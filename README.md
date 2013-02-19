# frostalert

frostalert is a simple tool which can be used to notify you when the temperature in your area is forecasted to go below
some given temperature. The tool uses wunderground.com for weather forecasts and gmail for sending the email, so you'll
need accounts on both of those sites in order to properly use it.

## Setup

You'll need an api key from wunderground.com. They're free for the developer ones, which is probably what you want.
You'll also need a gmail account, again free. If you're using two-factor authentication for your gmail you need to make
an application specific password for frostalert and use that for your gmail password instead.

* Get [leiningen](https://github.com/technomancy/leiningen)
* `git clone https://github.com/mediocregopher/frostalert.git`
* `lein uberjar`

## Usage

Once you've run `lein uberjar` the you can pick out the standalone jar file from the target directory that's been made
in the project root. You can then run `java -jar frostalert-0.1.0-SNAPSHOT-standalone.jar -h` to see the help options.

Here's an example usage of frostalert:
```bash
java -jar frostalert-0.1.0-SNAPSHOT-standalone.jar --api-key wundergroundkey \
                                                   --gmail-user mediocregopher \
                                                   --gmail-pass wouldntyouliketoknow \
                                                   --state TX \
                                                   --city houston \
                                                   --temperature 65 \
                                                   --email-from mediocregopher@gmail.com \
                                                   mediocregopher@gmail.com gophersfriend@gmail.com
```

This will check if the temperature on the current day is forecasted to go below 65, and send an email to
mediocregopher@gmail.com and gophersfriend@gmail.com if it is. It will also send an email if there's any problems at any
point in the process. The idea is to stick this in a cron that will run every day and forget about it.

## License

Copyright © 2013 Brian Picciano

Distributed under the Eclipse Public License, the same as Clojure.
