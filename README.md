# dotalys-cli
CLI version of the Dotalys  parser for Dota 2 replays

This project is based on Dotalys, a parser for Dota 2 replays, which was written by Tobias Mahlmann and is hosted here https://code.google.com/p/dotalys2/

Our modifications have been written by Yohan Bacquey and Thomas Graviou.

# Usage
<code>java -jar dotalys-cli.jar replay.dem [option]</code>

You can only download the json or build it via <code>ant</code>

## List of options
<code>-movement</code>  coordinates through time                                        <br/>
<code>-gold</code>      amount of gold through time                                     <br/>
<code>-xp</code>        experience points through time                                  <br/>
<code>-ability</code>   abilty uses, coordinates of the source, name of the ability     <br/>
<code>-item</code>      item uses, coordinates of the source, name of the item          <br/> 
<code>-death</code>     death coordinates through time                                  <br/>


# How it works
We have added an architecture of logs to obtain players data in json form on stdout.
They can be found in the <code>de.lighti.log</code> package.

First the given replay is parsed and a model of the game is filled with replay data.
Then each log extract itself the data it needs from this model and prints it to stdout.

