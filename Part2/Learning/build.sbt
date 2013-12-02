import AssemblyKeys._ // put this at the top of the file
// import com.github.philcali.DoccoPlugin.docco
import sbtassembly.Plugin._

// Dependency graphs
// net.virtualvoid.sbt.graph.Plugin.graphSettings

// Docco plugin settings
// seq(doccoSettings: _*)

// docco.title := "Songride Analyzer"

// docco.skipEmpty := false

// Assembly plugin settings
assemblySettings

mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
    {
        case "META-INF/MANIFEST.MF" => MergeStrategy.discard
        case "META-INF/README.txt" => MergeStrategy.discard
        case "META-INF/CHANGES.txt" => MergeStrategy.discard
        case "META-INF/NOTICE" => MergeStrategy.discard
        case "META-INF/NOTICE.TXT" => MergeStrategy.discard
        case "META-INF/LICENSE" => MergeStrategy.concat
        case "META-INF/LICENSE.txt" => MergeStrategy.concat
        case "META-INF/LICENSES.txt" => MergeStrategy.concat
        case x => old(x)
    }
}

// set the name of the project
name := "spamfilter-learning"

version := "0.1.0"

scalaVersion := "2.10.3"

organization := "com.xhochy"

mainClass := Some("com.xhochy.App")

libraryDependencies += "junit" % "junit" % "4.11"

scalacOptions ++= Seq("-unchecked", "-deprecation")

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

// https://groups.google.com/forum/?hl=en#!activity/liftweb/Um5ghzYMDUoJ/liftweb/DDTzzxRbCNU/qEo0lIbTv4kJ
// needed for javaMail 1.4.4
resolvers += "Java.net Maven2 Repo" at "http://download.java.net/maven/2/"


// seq(ScctPlugin.scctSettings: _*)

// seq(WebPlugin.webSettings: _*)
// seq(webSettings :_*)

// for jrebel
// jettyScanDirs := Nil
// scanDirectories in Compile := Nil

// needed to stop a clash between slf4j-log4j12 and logback-classic
// ivyXML := <dependencies>
// <dependency org="eu.medsea.mimeutil" name="mime-util" rev="2.1.3" >
// <exclude module="slf4j-log4j12" />
// </dependency>
// </dependencies>

// add compile dependencies on some dispatch modules
// libraryDependencies ++= Seq(
	// "net.databinder" %% "dispatch-meetup" % "0.7.8",
	// "net.databinder" %% "dispatch-twitter" % "0.7.8"
// )

// Set a dependency based partially on a val.
// {
  // val libosmVersion = "2.5.2-RC1"
  // libraryDependencies += "net.sf.travelingsales" % "osmlib" % libosmVersion from "http://downloads.sourceforge.net/project/travelingsales/libosm/"+libosmVersion+"/libosm-"+libosmVersion+".jar"
// }

// reduce the maximum number of errors shown by the Scala compiler
// maxErrors := 20

// increase the time between polling for file changes when using continuous execution
// pollInterval := 1000

// append several options to the list of options passed to the Java compiler
// javacOptions ++= Seq("-source", "1.5", "-target", "1.5")

// append -deprecation to the options passed to the Scala compiler
// scalacOptions += "-deprecation"

// define the statements initially evaluated when entering 'console', 'console-quick', or 'console-project'
// initialCommands := """
  // import System.{currentTimeMillis => now}
  // def time[T](f: => T): T = {
    // val start = now
    // try { f } finally { println("Elapsed: " + (now - start)/1000.0 + " s") }
  // }
// """

// set the initial commands when entering 'console' only
// initialCommands in console := "import damianhelme._"

// set the main class for packaging the main jar
// 'run' will still auto-detect and prompt
// change Compile to Test to set it for the test jar
// mainClass in (Compile, packageBin) := Some("myproject.MyMain")

// set the main class for the main 'run' task
// change Compile to Test to set it for 'test:run'
// mainClass in (Compile, run) := Some("myproject.MyMain")

// add <base>/input to the files that '~' triggers on
// watchSources <+= baseDirectory map { _ / "input" }

// add a maven-style repository
// resolvers += "name" at "url"

// add a sequence of maven-style repositories
// resolvers ++= Seq("name" at "url")

// define the repository to publish to
// publishTo := Some("name" at "url")

// set Ivy logging to be at the highest level
// ivyLoggingLevel := UpdateLogging.Full

// disable updating dynamic revisions (including -SNAPSHOT versions)
// offline := true

// set the prompt (for this build) to include the project id.
// shellPrompt in ThisBuild := { state => Project.extract(state).currentRef.project + "> " }

// set the prompt (for the current project) to include the username
// shellPrompt := { state => System.getProperty("user.name") + "> " }

// disable printing timing information, but still print [success]
// showTiming := false

// disable printing a message indicating the success or failure of running a task
// showSuccess := false

// change the format used for printing task completion time
// timingFormat := {
	// import java.text.DateFormat
	// DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)
// }

// disable using the Scala version in output paths and artifacts
// crossPaths := false

// fork a new JVM for 'run' and 'test:run'
// fork := true

// fork a new JVM for 'test:run', but not 'run'
// fork in Test := true

// add a JVM option to use when forking a JVM for 'run'
// javaOptions += "-Xmx2G"

// only use a single thread for building
// parallelExecution := false

// Execute tests in the current project serially
//   Tests from other projects may still run concurrently.
// parallelExecution in Test := false

// set the location of the JDK to use for compiling Java code.
// if 'fork' is true, this is used for 'run' as well
// javaHome := Some(file("/usr/lib/jvm/sun-jdk-1.6"))

// Use Scala from a directory on the filesystem instead of retrieving from a repository
// scalaHome := Some(file("/home/user/scala/trunk/"))

// don't aggregate clean (See FullConfiguration for aggregation details)
// aggregate in clean := false

// only show warnings and errors on the screen for compilations.
//  this applies to both test:compile and compile and is Info by default
// logLevel in compile := Level.Info

// only show warnings and errors on the screen for all tasks (the default is Info)
//  individual tasks can then be more verbose using the previous setting
logLevel := Level.Info

// only store messages at info and above (the default is Debug)
//   this is the logging level for replaying logging with 'last'
// persistLogLevel := Level.Debug

// only show 10 lines of stack traces
// traceLevel := 10

// only show stack traces up to the first sbt stack frame
// traceLevel := 0

// add SWT to the unmanaged classpath
// unmanagedJars in Compile += file("/usr/share/java/swt.jar")

// publish test jar, sources, and docs
// publishArtifact in Test := true

// disable publishing of main docs
// publishArtifact in (Compile, packageDoc) := false

// change the classifier for the docs artifact
// artifactClassifier in packageDoc := Some("doc")

// Copy all managed dependencies to <build-root>/lib_managed/
//   This is essentially a project-local cache and is different
//   from the lib_managed/ in sbt 0.7.x.  There is only one
//   lib_managed/ in the build root (not per-project).
// retrieveManaged := true


