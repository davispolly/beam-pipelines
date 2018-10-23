package com.example;

import com.google.common.base.Splitter;
import org.apache.beam.sdk.metrics.Counter;
import org.apache.beam.sdk.metrics.Metrics;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.values.TupleTag;
import org.json.JSONObject;

import java.util.List;

public class ExtractCustomDoFn extends DoFn<String, String> {

  public static final TupleTag<String> EXTRACTCUSTOM_SUCCESS = new TupleTag<String>() {};
  public static final TupleTag<String> EXTRACTCUSTOM_FAILED = new TupleTag<String>() {};
  private static final List<String> VALID_CUSTOM_TYPES = Splitter.on(',').splitToList(JsonToBQ.options.getValidCustomDataTypes().get());
  private static final String CUSTOMDATATYPE_FIELDSELECTOR = JsonToBQ.options.getCustomDataTypeFieldSelector().get();
  private static final String CUSTOMDATATYPE_EXCLUDING_FIELDSELECTOR = JsonToBQ.options.getCustomDataTypeExcludingFieldSelector().get();
  private final List<String> filter;
  private final String customDataType;
  private final Counter extractedCustomRows;
  private final Counter discardedCustomRows;
  private final Counter failedExtractCustomRows;


  public ExtractCustomDoFn(String customDataType, String customFields) {
    this.customDataType = customDataType;
    if (VALID_CUSTOM_TYPES.stream().noneMatch(this.customDataType::equals))
      throw new RuntimeException(
          "Initialization Error. Invalid Custom Data Type "
              + this.customDataType
              + " provided. Please check");
    this.filter = Splitter.on(',').omitEmptyStrings().trimResults().splitToList(customFields);
    this.extractedCustomRows = Metrics.counter(ExtractCustomDoFn.class, this.customDataType + "-row-counts");
    this.discardedCustomRows = Metrics.counter(ExtractCustomDoFn.class, "filteredout-row-counts");
    this.failedExtractCustomRows = Metrics.counter(ExtractCustomDoFn.class, "failed-customextract-row-counts");
  }

  @ProcessElement
  public void processElement(ProcessContext context) {
    JSONObject element = new JSONObject(context.element());
    if (element.has(CUSTOMDATATYPE_FIELDSELECTOR) && element.get(CUSTOMDATATYPE_FIELDSELECTOR) instanceof String) {
      if (this.customDataType.equals(CUSTOMDATATYPE_EXCLUDING_FIELDSELECTOR)
          && this.filter
              .stream()
              .noneMatch(element.getString(CUSTOMDATATYPE_FIELDSELECTOR)::equals)) {
        context.output(EXTRACTCUSTOM_SUCCESS, context.element());
        this.extractedCustomRows.inc();
      } else if (this.filter
          .stream()
          .anyMatch(element.getString(CUSTOMDATATYPE_FIELDSELECTOR)::equals)) {
        context.output(EXTRACTCUSTOM_SUCCESS, context.element());
        this.extractedCustomRows.inc();
      } else {
        this.discardedCustomRows.inc();
      }
    } else {
      context.output(EXTRACTCUSTOM_FAILED, context.element());
      this.failedExtractCustomRows.inc();
    }
  }
}
