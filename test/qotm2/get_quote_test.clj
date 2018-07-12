(ns qotm2.get_quote_test
  (:require [clojure.test :refer :all]
            [qotm2.get_quote :refer :all]
            [qotm2.aws :as aws])
  (:import (java.io ByteArrayOutputStream ByteArrayInputStream)
           (java.nio.charset StandardCharsets)))

(deftest get-quote-test
  (testing "can get quotes."
    (is (=
          (with-redefs [bucket "bucket"
                        aws/get-object-as-json (fn m [bucket key] (cond (and (= bucket "bucket") (= key "index")) ["key"]
                                                                        (and (= bucket "bucket") (= key "key")) {"quote" "hello, clojure"}))]
            (get-quote nil))
          {"quote" "hello, clojure"}))))

(deftest handle-request-test
  (testing "works fine."
    (is (=
          (with-redefs [bucket "bucket"
                        aws/get-object-as-json (fn m [bucket key] (cond (and (= bucket "bucket") (= key "index")) ["key"]
                                                                        (and (= bucket "bucket") (= key "key")) {"quote" "hello, clojure"}))]
            (let [baos (ByteArrayOutputStream.)]
               (-handleRequest nil (ByteArrayInputStream. (byte-array 0)) baos nil)
               (String. (.toByteArray baos) StandardCharsets/UTF_8))))
          "{\"quote\":\"hello, clojure\"}")))
