# BIOfid Ontology Collector

This is currently under development. The main framework is implemented.

### Requirements
- Java 11+
- Maven 3.6+

### Compiling
To compile the code simply run

```shell
mvn package
```

This will also call the test suite.
To avoid running also the (fast) tests, you can call:

```shell
mvn package -DskipTests
```

## Usage
In the `config/general.json`, you find a test setting for the BIOfid Collector. The general configuration is expected to be at the mentioned path. Running the application is easy as:

```shell
java -jar biofid-ontology-collector.jar
```

### Configuration
There is a `config` folder, holding all necessary configuration files for the BIOfid Ontology Collector to run. The general setup happens in the `general.json` configuration file. There you define the name and the data services of the ontologies. Each data service configuration takes the (optional) kewords `dataSourceClass`, `dataGeneratorClass`, and `dataProcessorClass`. However, at least a valid `dataGeneratorClass` or `dataProcessorClass` has to be given! 

Additionally, the data service configuration takes the (also optional) keyword `parameters`. The latter holds the configuration data that is handed directly to the respective processing unit.

E.g., this configuration:

```json
...
"dataProcessorClass": "de.biofid.services.data.processors.FilterDataProcessor",
"parameters": {
    "dataGenerator": {"ids": ["2882316"]},
    "dataProcessor": {"configurationFile": "config/generators/predicate-filter.json"}
}
```

will hand the data in the `parameters` -> `dataProcessor` section to the unit instantiated by the `dataProcessorClass` (in this case a `FilterDataProcessor`).

Please refer the example configuration in the `config` folder for more details.

### Namespaces
If you want to define namespaces that are used in your ontology and should be abbreviated in the final output, you have to define them in a file `config/namespaces.json`. The file has to be exactly that. Currently, there is no need to make this customizable. Please open an issue, if you think otherwise. There is already an example namespace file given for you to modify.

### Filter triples
When applying the `FilterDataProcessor`, you can filter out triples. You can either filter for desired Predicates (i.e., storing only these in the ontology), giving the key `desiredPredicates` in a configuration file that is passed to the DataProcessor, or filter out unwanted Predicates with the `unwantedPredicates` key. Each of the keywords should be followed by a list of predicate terms (see `config/predicate-filter.json` for an example).

It is redundant to give both keys in the same configuration file. If you give a list with `desiredPredicates`, only these will be stored in the ontology. All others will be sorted out. If both keys are given, `desiredPredicates` has precedence!

## Concepts
This tool provides a highly configurable way to harvest a data source and generate an ontology from the data. For this purpose, the ontology processing is split into multiple steps, each processed by a DataService class. The user can define multiple of DataService classes, each adding more information from separate data sources or further processing the data already in the ontology.

### DataService
A DataService contains (in this order of processing) a DataSource, DataGenerator, and a DataProcessor. Finally, it returns a list of Triple objects that are serialized in the ontology.

#### DataSource
A DataSource is simply the source for the data. It can read a file or call data from a web API. The DataSource hands the received data directly to the DataGenerator.

#### DataGenerator
The DataGenerator uses the input from the DataSource to generate Triples from it. This should be as naive as possible. The generated Triple objects are then handed to the DataProcessor.

#### DataProcessor
The DataProcessor is responsible for any meaningful mappings of the Triple attributes (Subject, Predicate, Object). Also, other, more sophisticated, data processing should be in this class.

##### Why not having lists of DataProcessors?
Currently, you have to define multiple DataServices, even if you only use the DataProcessor in each (leaving DataSource and DataGenerator empty). This comes from me having currently no idea how to parameterize the DataProcessors in this list appropriately.

So, when having this configuration:

```json
...
"services": [
  {
    "dataSourceClass": "de.biofid.services.data.sources.http.JsonHttpApi",
    "dataGeneratorClass": "de.biofid.services.data.generators.gbif.GbifGenericDataGenerator",
    "dataProcessorClass": [
      "de.biofid.services.data.processors.FilterDataProcessor",
      "de.biofid.services.data.processors.PredicateMappingDataProcessor"
    ]
    ...
  }
]
```

How can you set the configuration of e.g., DataProcessor #2 but not of DataProcessor #1 (perhaps it does not need configurations)? Also, it has to stay readable and maintainable (both code and configuration file). So, if you have a cool idea, please do not hesitate to open an issue or make a Pull Request!

## Licence
![AGPL-3.0 License](https://www.gnu.org/graphics/agplv3-88x31.png)

## Citation
Please cite the Ontology Collector in your publication with:
```
Pachzelt A. (2021). BIOfid Ontology Collector [Source Code]. Available from https://dev.git.ub.uni-frankfurt.de/ublabs/OntologyCollector.
```

BibTeX:
```
@misc{pachzelt2021, title={BIOfid Ontology Collector [Source Code]}, url={https://dev.git.ub.uni-frankfurt.de/ublabs/OntologyCollector}, author={Pachzelt, Adrian}, year={2021}} 
```

## TODO
* All processes are still only separate and have to me intertwined in the main().
* Currently, there is no default for any of these classes. Hence, when no DataSource is given, it would be nice, if the DataSource hands over either the whole ontology or iterates the triples in the ontology.