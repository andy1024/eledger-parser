# eledger-parser
School e-ledger extracting and parsing tool to be used by parents in order to monitor their child's progress in education

1. Use case

   I wanted to be sure that nothing that is posted on my kids' e-ledger gets unnoticed: homeworks, important messages, test annoucements. So I designed this little tool to extract all the relevant information, pack it to a document as short and concise as possible, and send it automatically to thermal printer that sits in the room for anyone to notice. The printer had to cut the paper itself, letting it to be picked by me or my wife as a set of direction for a homework tasked to our kids.

   Why I didn't consider opening the ledger's website on a smartphone and reading relevant info from it? Two reasons: smartphone easily distracts children and website isn't particularly well designed to browse on a small screen.
   Third, implicit, reason was to make something cool with my thermal printer, that wasn't just a bunch of silly images on a printout.

2. Design

   Many of the tool's features are answers to my personal needs, that no one else would care about. However, I tried to make it as extensible as it gets. Whole thing is customizable by a simple properties file, including output format, formatter filter, event handlers and many, many more.

3. Usage
  1. Prerequisities  
   In its OOB form, the software needs Java 7, LaTeX software with texlive-lang-polish, texlive-fonts-recommended and some printer to handle the output. You can however skip the preformatting part and send a plain text page to a generic CUPS printer by simple tweaks in properties file.
   Of course you would also need an account on a e-ledger website that is run on a specific engine. For various reasons I'm not stating the default website manufacturer name. Feel free to ask, if you are interested in extending the tool to suit your needs.
  2. Preconfiguration  
   Edit the config.properties file and change the following keys to match your environment:

   - sys.output.printer : enter your CUPS printer name here
   - sys.output.formatter : enter your formatter class of choice here (implements Formatter interface)
   - web.baseUrl : base URL of your e-ledger website
   - auth.data : semicolon separated list of authentication pairs "user,pass"

3. Building

   The tool main build management software is maven. 
   Build using 'mvn install' command

4. Running the tool

   The tool is configured with MANIFEST file, so you can run it by entering the simple command
   'java -jar <artifact-name>', where artifact name is by default eledger-parser-with-dependencies.jar

   In my intention it was supposed to be run from cron periodically, to make sure all important information awaits when we get home.

5. Output

   In the stdout you'll see some informational messages about program workings. First it will read the config, then access the datastore, next download the necessary information from the server, check what's and finally output the extract on the device specified in the configuration file.
  1. Sample output  
```
Event fired: app.event.start
Event handler class org.warheim.app.Application$1
application start
Event handler finished
/home/andy/.eledger/datastore.json
Nov 29, 2015 3:29:45 PM org.warheim.di.ObjectFactory createObject
INFO: null
Nov 29, 2015 3:29:45 PM org.warheim.di.ObjectFactory createObject
INFO: null
Nov 29, 2015 3:29:45 PM org.warheim.di.ObjectFactory createObject
INFO: null
Nov 29, 2015 3:29:45 PM org.warheim.di.ObjectFactory createObject
INFO: null
Event fired: app.event.afterConfigRead
All notifications
for user: Matthew C. Cornwallis
Tasks
Maths and algebra
 2015-10-10 Memorize multiplication matrix
 2015-10-12 Complex numbers calculus using algebraic theorems
English language
 2015-11-12 Learn to cite any Shakespeare play
Tests
Maths and algebra
 2015-11-30 All algebra test
Messages
 111 2015-09-01 john@mit.edu all-students No more jumping on the bed!

Event fired: app.event.beforeDataStoreWrite
Event fired: app.event.afterDataStoreWrite
Event fired: app.event.finish
```

6. Problems and troubleshooting

   Nothing here for the moment

7. What's next

   - [ ] support more sources
   - [ ] make more formatters and output destinations (such as an e-mail or alphanumerical LCD screen connected to a Raspberry PI)
   - [x] support custom configuration
   - [x] handle logouts
   - [ ] add custom user name support
   - [x] add icons to PdfLatexFormatter
