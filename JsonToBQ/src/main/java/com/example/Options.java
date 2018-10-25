/*
#
# Copyright (C) 2018 Google Inc.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
*/

package com.example;

import org.apache.beam.runners.dataflow.options.DataflowPipelineOptions;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryOptions;
import org.apache.beam.sdk.options.ApplicationNameOptions;
import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.Validation;
import org.apache.beam.sdk.options.ValueProvider;

public interface Options
    extends ApplicationNameOptions, PipelineOptions, DataflowPipelineOptions, BigQueryOptions {

  @Description("Input Directory")
  @Validation.Required
  ValueProvider<String> getInputDirectory();

  void setInputDirectory(ValueProvider<String> value);

  @Description("Input Filename Suffix")
  @Default.String(".gz")
  ValueProvider<String> getInputFilenameSuffix();

  void setInputFilenameSuffix(ValueProvider<String> value);

  @Description("BigQuery Schema File")
  @Validation.Required
  ValueProvider<String> getBQSchemaFile();

  void setBQSchemaFile(ValueProvider<String> value);

  @Description("JSON Schema File")
  @Validation.Required
  ValueProvider<String> getJSONSchemaFile();

  void setJSONSchemaFile(ValueProvider<String> value);

  @Description("Job Tag Prefix")
  @Default.String("bq-load")
  ValueProvider<String> getJobTagPrefix();

  void setJobTagPrefix(ValueProvider<String> value);

  /* TODO: Make this more generic prefix */
  /*(Needs to be DATE, TIMESTAMP since it is used for Partitioning)
   */
  @Description(
      "Comma Separated List of Prefixes with values in the format YYYY-mm-dd. This is used to create file pattern")
  @Validation.Required
  ValueProvider<String> getInputPrefixes();

  void setInputPrefixes(ValueProvider<String> value);

  @Description("Output BigQuery Dataset Name")
  @Validation.Required
  ValueProvider<String> getOutputDatasetName();

  void setOutputDatasetName(ValueProvider<String> value);

  @Description("BigQuery Output Table Name")
  @Validation.Required
  ValueProvider<String> getOutputTableName();

  void setOutputTableName(ValueProvider<String> value);

  @Description("BigQuery Error Table Name")
  @Validation.Required
  ValueProvider<String> getErrorTableName();

  void setErrorTableName(ValueProvider<String> value);

  @Description("BigQuery Partition Column Name for Output Table")
  @Validation.Required
  ValueProvider<String> getOutputTablePartitionColumn();

  void setOutputTablePartitionColumn(ValueProvider<String> value);

  @Description("BigQuery Partition Column Name for Error Table")
  @Validation.Required
  ValueProvider<String> getErrorTablePartitionColumn();

  void setErrorTablePartitionColumn(ValueProvider<String> value);

  @Description(
      "Input Timestamp Field Name from which the BigQuery Partition Column value is derived")
  @Validation.Required
  ValueProvider<String> getTimestampColumn();

  void setTimestampColumn(ValueProvider<String> value);

  @Description("BigQuery Partition Column Date Format. Defaults to yyyy-MM-dd")
  @Default.String("yyyy-MM-dd")
  ValueProvider<String> getPartitionColumnDateFormat();

  void setPartitionColumnDateFormat(ValueProvider<String> value);

  @Description("Comma Separated List of Sensitive Fields to Encrypt")
  @Validation.Required
  ValueProvider<String> getSensitiveFields();

  void setSensitiveFields(ValueProvider<String> value);

  @Description("Root Field Name which contains Geo Co-Ordinates")
  @Default.String("geo")
  ValueProvider<String> getGeoRootFieldName();

  void setGeoRootFieldName(ValueProvider<String> value);

  @Description(
      "Comma Separated List of Custom Fields to Extract. This is required for Custom Data only. Default value is not used at all")
  @Default.String("")
  ValueProvider<String> getFilterCustomFields();

  void setFilterCustomFields(ValueProvider<String> value);

  @Description(
      "Custom Data Type can be one of valid custom datatypes. This is required for Custom Data only. Default value is not used at all")
  @Default.String("")
  ValueProvider<String> getCustomDataType();

  void setCustomDataType(ValueProvider<String> value);

  @Description(
      "List of all possible custom data types. This is required for Custom Data only. Default value is not used at all")
  @Default.String("")
  ValueProvider<String> getValidCustomDataTypes();

  void setValidCustomDataTypes(ValueProvider<String> value);

  @Description(
      "Field used to clasify custom data types. This is required for Custom Data only. Default value is not used at all")
  @Default.String("")
  ValueProvider<String> getCustomDataTypeFieldSelector();

  void setCustomDataTypeFieldSelector(ValueProvider<String> value);

  @Description(
      "Value used to clasify custom data types when exclusion is in place. This is required for Custom Data only. Default value is not used at all")
  @Default.String("")
  ValueProvider<String> getCustomDataTypeExcludingFieldSelectorValue();

  void setCustomDataTypeExcludingFieldSelectorValue(ValueProvider<String> value);

  @Description(
      "Indicates if the pipeline should sanitize json property names, making it sure they're valid BQ column names.")
  @Default.Boolean(false)
  ValueProvider<Boolean> getSanitizeJson();

  void setSanitizeJson(ValueProvider<Boolean> value);

  @Description(
      "Defines if the custom data portion of the information will be converted as a String. This is required for Custom Data only. Default value is not used at all")
  @Default.Boolean(false)
  ValueProvider<Boolean> getStringifyCustomData();

  void setStringifyCustomData(ValueProvider<Boolean> value);

  @Description(
      "Name of the property that holds the custom data. This is required for Custom Data only. Default value is not used at all")
  @Default.String("")
  ValueProvider<String> getCustomDataField();

  void setCustomDataField(ValueProvider<String> value);
}
