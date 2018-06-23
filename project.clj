(defproject qotm2 "0.1.0-SNAPSHOT"
  :main ^:skip-aot qotm2.core
  :dependencies [[org.clojure/clojure "1.8.0"]
                 ;[com.google.guava/guava "25.1-jre"]
                 [com.amazonaws/aws-lambda-java-core "1.2.0"]
                 [com.amazonaws/aws-java-sdk-s3 "1.11.346"]
                 [cheshire "5.8.0"]
                 ]
  :profiles
  {:dev     {:dependencies [[org.mockito/mockito-core "2.18.3"]]}
   :uberjar {:aot :all}})
