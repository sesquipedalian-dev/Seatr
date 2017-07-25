# Seatr

App for arranging seating for an event based on relationships of the attendees and their interests using a genetic algorithm.  

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. 
### Prerequisites

What things you need to install the software and how to install them

```
JDK 1.8+
Google Account (for inputting attendee data in a spreadsheet)
IntelliJ IDEA 2016.3.6+
```

### Installing

* clone the repo
* open the trunk directory as a IDEA project

## Running the tests

* run the 'default' run configuration in IntelliJ

### Test Output

By default, the main class will run using the test data in the /testData directory of the repo.  The command line output will be something like this: 

```
GA inputs {0}{15}{900}{4}
Score after generation: {0 ms}{0}/{100}: {741}/{900}
.
.
.
Score after generation: {2 ms}{100}/{100}: {841}/{900}
GA complete.  Total time: {350 ms}, generations {100}
******FINAL******

Table 1 (CIRCLE/7):
	Seat 1 (109): Person 5
		Score explanation:
			Seated next to Person 9:
				(-10): Person 5's tag {sports} relates to Person 9's tag {videoGames} with compatibility {NEGATIVE} for a score of {-10}.
				(20): Person 5's tag {videoGames} relates to Person 9's tag {videoGames} with compatibility {IDENTITY} for a score of {20}.
				(10): Person 5's tag {nerd} relates to Person 9's tag {videoGames} with compatibility {POSITIVE} for a score of {10}.
			Seated next to Person 6:
				(20): Person 5's tag {youngChildren} relates to Person 6's tag {youngChildren} with compatibility {IDENTITY} for a score of {20}.
				(20): Person 5's tag {socialRightist} relates to Person 6's tag {socialRightist} with compatibility {IDENTITY} for a score of {20}.
				(10): Person 5's tag {socialRightist} relates to Person 6's tag {fiscalRightist} with compatibility {POSITIVE} for a score of {10}.
				(-10): Person 5's tag {sports} relates to Person 6's tag {geek} with compatibility {NEGATIVE} for a score of {-10}.
				(10): Person 5's tag {videoGames} relates to Person 6's tag {geek} with compatibility {POSITIVE} for a score of {10}.
				(10): Person 5's tag {fiscalRightist} relates to Person 6's tag {socialRightist} with compatibility {POSITIVE} for a score of {10}.
				(20): Person 5's tag {fiscalRightist} relates to Person 6's tag {fiscalRightist} with compatibility {IDENTITY} for a score of {20}.
				(10): Person 5's tag {nerd} relates to Person 6's tag {geek} with compatibility {POSITIVE} for a score of {10}.
			Seated at same table as Person 1:
				(2): Person 5's tag {youngChildren} relates to Person 1's tag {youngChildren} with compatibility {IDENTITY} for a score of {2}.
				(-1): Person 5's tag {sports} relates to Person 1's tag {nerd} with compatibility {NEGATIVE} for a score of {-1}.
				(1): Person 5's tag {videoGames} relates to Person 1's tag {nerd} with compatibility {POSITIVE} for a score of {1}.
				(2): Person 5's tag {nerd} relates to Person 1's tag {nerd} with compatibility {IDENTITY} for a score of {2}.
			Seated at same table as Person 2:
				(2): Person 5's tag {youngChildren} relates to Person 2's tag {youngChildren} with compatibility {IDENTITY} for a score of {2}.
				(2): Person 5's tag {sports} relates to Person 2's tag {sports} with compatibility {IDENTITY} for a score of {2}.
				(1): Person 5's tag {sports} relates to Person 2's tag {workOut} with compatibility {POSITIVE} for a score of {1}.
				(-1): Person 5's tag {videoGames} relates to Person 2's tag {sports} with compatibility {NEGATIVE} for a score of {-1}.
				(-1): Person 5's tag {nerd} relates to Person 2's tag {sports} with compatibility {NEGATIVE} for a score of {-1}.
				(-2): Person 5 is aged {28} years, falling into age bracket {4}.  Person 2 is aged {35}, falling into age bracket {5}.  This gives them a bracket difference of {1}, for a score of {-2}
			Seated at same table as Person 7:
				(1): Person 5's tag {youngChildren} relates to Person 7's tag {grownChildren} with compatibility {POSITIVE} for a score of {1}.
				(-4): Person 5 is aged {28} years, falling into age bracket {4}.  Person 7 is aged {62}, falling into age bracket {6}.  This gives them a bracket difference of {2}, for a score of {-4}
			Seated at same table as Person 8:
				(1): Person 5's tag {youngChildren} relates to Person 8's tag {grownChildren} with compatibility {POSITIVE} for a score of {1}.
				(2): Person 5's tag {sports} relates to Person 8's tag {sports} with compatibility {IDENTITY} for a score of {2}.
				(-1): Person 5's tag {videoGames} relates to Person 8's tag {sports} with compatibility {NEGATIVE} for a score of {-1}.
				(-1): Person 5's tag {nerd} relates to Person 8's tag {sports} with compatibility {NEGATIVE} for a score of {-1}.
				(-4): Person 5 is aged {28} years, falling into age bracket {4}.  Person 8 is aged {60}, falling into age bracket {6}.  This gives them a bracket difference of {2}, for a score of {-4}
	Seat 2 (113): Person 6
```

Let's break that down a little: 

```
GA inputs {0}{15}{900}{4}
```
This is just echoing back some of the parameters used by the genetic algorithm.

```
Score after generation: {0 ms}{0}/{100}: {741}/{900}
Score after generation: {32 ms}{1}/{100}: {772}/{900}
```
The genetic algorithm works by taking an initial population of possible seating arrangements, and modifying them with subsequent generations.  Each iteration of the GA, it outputs the score of the best arrangement out of the threshold score (the GA will terminate after a score of at least the threshold is found).  This output is helpful to see that the algorithm is going somewhere; hopefully, the score will start out somewhat low and increase in each generation until it stabilizes around the best solution.

```
Table 1 (CIRCLE/7):
``` 
The first entry is table 1, a circular table with 7 seats.

```
Seat 1 (109): Person 5
```
The first seat at that table, with a score of 109, is Person 5.

```
		Score explanation:
			Seated next to Person 9:
				(-10): Person 5's tag {sports} relates to Person 9's tag {videoGames} with compatibility {NEGATIVE} for a score of {-10}.
				(20): Person 5's tag {videoGames} relates to Person 9's tag {videoGames} with compatibility {IDENTITY} for a score of {20}.
				(10): Person 5's tag {nerd} relates to Person 9's tag {videoGames} with compatibility {POSITIVE} for a score of {10}.
```
What follows is the explanation of the 109 score we assigned.  Each person at the same table will have a weighted contribution to the score, with people sitting 'next to' or 'across' having more weight than just 'at the same table'.  This entry is for Person 5 being seated next to Person 9.  It's explaining that some score was assigned for the interest tags that are assigned to each attendee.  The relationships between tags are specified in the ' - Tags.csv' input file.

After that, we get the score for all the other people seated at that table and their impact on Person 5's score. After that, we'll get to 'Seat 2' and it explains the score for Person 6 seated next to Person 5. 

```
	Table Score: (304)
```
This reports the score for one table. 

```
TOTAL SCORE: (841)
```
This is the final output of the program; it tells you the total score that was calculated for the seating arrangement it decided on.

## Supplying data

In order to generate useful output, you'll want to provide your own CSVs containing the info on the attendees for your event.  

### Create your own data spreadsheet (Optional)
I recommend using google sheets to produce your csv input, since I know the format google puts out will be understood by Seatr. 

#### Clone the public sheet

* navigate to the public formatting sheet for seatr: https://docs.google.com/spreadsheets/d/10sc95gGyH2Ui1YlkfqLdnpza3mWF5zcNrZbtN2i8Gdw/edit?usp=sharing
* make a copy of that sheet for your use
* fill in the data for your attendees.  You may modify the 'People', 'Relationships', 'Tables', and 'Tags' tabs.

#### People data
* Guest	- name of attendee that will show up in output
* party leader - if you want a group of people to be seated together, give them all the same party leader (which is the name of one of the guests in that party)
* age	- age of attendee - attendees far enough apart in age get a negative score on their relationships
* tags - comma-separated list of 'tags' for the user.  Each tag should be defined on the tags tab of your spreadsheet.  Tags are used to score attendee's shared interests (or interests that are incompatible, sometimes)
* pegged table - if you want to force an attendee into a particular table / seat, fill that in here
* pegged seat - if you want to force an attendee into a particular table / seat, fill that in here

#### Tables data
* Table ID - identifier for table (shows up in output)
* Shape - enumerated value for table shape. 'Circular', 'Rectangle' (people on both sides and one on each end), 'rectangle_one_sided' (people on only one side of rectangle), 'rectangle_no_end' (people on both sides but not ends)
* Seat Count - number of seats that table supports

#### Relationships data
* Party A - first person in relationship
* Party B - second person in relationship
* Type - enumerated value for type of relationship: hates, besties, likes, dislikes

#### Tags data
* Tag name - string name of tag
* positive - comma-separated list of other tags that this tag likes
* weakly positive - comma-separated list of other tags that this tag likes a little
* weakly negative - comma-separated list of other tags that this tag dislikes a little
* negative - comma-separated list of other tags that this tag dislikes
* user created - always false

#### Export to CSV
* On each of the 4 tabs, use the menu item 'File' -> 'Download as' -> 'Comma-separated values'.  
* save to the relevant file in the testData directory of the app

#### Tweak algorithm details
* You may need to tweak the genetic algorithm's parameters to get the best result.  Look at the values in GeneticSeatAssigner.java and tweak them as desired.  Particularly, the THRESHOLD_SCORE_PER_PERSON will depend heavily on how many tags you give to each person.  Also, the POPULATION_SIZE and GENERATIONS will have a large impact on the time to run the calculation.  

#### Generate results
* In IntelliJ, run the default run config
* Review the output.  You may want to run it a few times and review the score achieved.  Hopefully a few runs will have a score within a fairly close range.  If not, you may want to tweak the other parameters on the GeneticSeatAssigner: elitist %, mutation %, crossover %, etc.  

## Built With
Seatr is built on just Java 1.8.  

## Authors

* **The Sesquipedalian Dev** - (https://github.com/sesquipedalian-dev)

See also the list of [contributors](https://github.com/your/project/contributors) who participated in this project.

## License

Apache License 2.0

## Acknowledgments

* wikipedia for a refresher on the genetic algorithm - https://en.wikipedia.org/wiki/Genetic_algorithm
