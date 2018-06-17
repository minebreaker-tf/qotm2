(ns qotm2.get_quote_test
  (:require [clojure.test :refer :all]
            [qotm2.get_quote :refer :all]
            [clojure.java.io :refer :all])
  (:import (java.io ByteArrayOutputStream ByteArrayInputStream)
           (java.nio.charset StandardCharsets)))

(deftest get-quote-test

  (testing "get-quote"
    (is (= (get-quote nil) {"result" "hello, clojure"}))))

(defn str-to-is [^String str]
  (-> str
    (.getBytes StandardCharsets/UTF_8)
    (ByteArrayInputStream.)))

(deftest handle-request-test

  (testing "works fine"
    (is (= (let [baos (ByteArrayOutputStream.)]
             (-handleRequest nil (str-to-is "{}") baos nil)
             (String. (.toByteArray baos) StandardCharsets/UTF_8)))
      "hello, clojure")))
