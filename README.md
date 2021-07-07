# BIOfid Ontology Collector

This is currently under development. The main framework is implemented.

## Concept
This tool provides a highly configurable way to harvest a data source and generate an ontology from the data. For this purpose, the ontology processing is split into multiple steps, each processed by a DataService class. The user can define multiple of DataService classes, each adding more information from separate data sources or further processing the data already in the ontology.

### DataService
A DataService contains (in this order of processing) a DataSource, DataGenerator, and a DataProcessor. Finally, it returns a list of Triple objects that are serialized in the ontology.

#### DataSource
A DataSource is simply the source for the data. It can read a file or call data from a web API. The DataSource hands the received data directly to the DataGenerator.

#### DataGenerator
The DataGenerator uses the input from the DataSource to generate Triples from it. This should be as naive as possible. The generated Triple objects are then handed to the DataProcessor.

#### DataProcessor
The DataProcessor is responsible for any meaningful mappings of the Triple attributes (Subject, Predicate, Object). Also, other, more sophisticated, data processing should be in this class.

## TODO
* All processes are still only separate and have to me intertwined in the main().
* Currently, there is no default for any of these classes. Hence, when no DataSource is given, it would be nice, if the DataSource hands over either the whole ontology or iterates the triples in the ontology.