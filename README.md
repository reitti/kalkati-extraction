# kalkati-extraction

A command-line app for extracting names and coordinates of stops in Kalkati.net XML database dumps.
For use primarily in the data update process of our search index.

## Usage

Build the uberjar: `lein uberjar`, and then run it with `java -jar`. It'll be in the `target` directory.

The program takes as arguments names of cities and the corresponding filenames into which the station
data should be added. For example:

    java -jar kalkati-extraction-0.0.1.jar Helsinki=/path/to/helsinki.txt Vantaa=/path/to/vantaa.txt

Each line written to the given files will be a stop name and its coordinates (lon,lat) separated by a pipe.

    Vuosaaren uimahalli|25.1416744,60.2088469
