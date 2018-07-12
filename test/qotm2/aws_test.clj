(ns qotm2.aws_test
  (:require [qotm2.aws :as aws]
            [clojure.test :refer :all]
            [cheshire.core :as cheshire])
  (:import (com.amazonaws.services.s3 AmazonS3)
           (com.amazonaws.auth AWSCredentialsProvider)
           (com.amazonaws.services.s3.model S3Object S3ObjectInputStream)
           (org.mockito Mockito)
           (java.io ByteArrayInputStream)
           (java.nio.charset StandardCharsets)
           (org.apache.http.client.methods HttpRequestBase)))

(deftest credential-test
  (testing "can get aws credential"
    (is (instance? AWSCredentialsProvider aws/credential))))

(deftest s3-test
  (testing "can get s3 client"
    (is (instance? AmazonS3 aws/s3))))

(defn mock-client
  ^AmazonS3
  ([^S3Object mock-object]
   (let [^AmazonS3 mock (Mockito/mock AmazonS3)]
     (.. (Mockito/when (.getObject
                         mock
                         ^String (Mockito/any)
                         ^String (Mockito/any)))
       (thenReturn mock-object))
     mock)))

(defn mock-http ^HttpRequestBase [] (Mockito/mock HttpRequestBase))

(defn mock-object ^S3Object [mock-result]
  (let [^S3Object mock (Mockito/mock S3Object)]
    (.. (Mockito/when (.getObjectContent mock))
      (thenReturn
        (S3ObjectInputStream.
          (ByteArrayInputStream.
            (.getBytes
              (cheshire/generate-string mock-result)
              StandardCharsets/UTF_8))
          (mock-http))))
    mock))

(deftest get-object-as-json-test
  (testing "get s3 object and returns it as json map."
    (is (=
          (with-redefs
            [qotm2.aws/s3 (mock-client (mock-object {"result" true}))]
            (aws/get-object-as-json "test-bucket" "test-object"))
          {"result" true}))))
