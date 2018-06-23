(ns qotm2.aws
  (:require [cheshire.core :as cheshire]
            [clojure.java.io :refer [reader]])
  (:import (com.amazonaws.auth DefaultAWSCredentialsProviderChain)
           (com.amazonaws.services.s3 AmazonS3ClientBuilder AmazonS3Client)
           (com.amazonaws.regions Regions)
           (com.amazonaws.services.s3.model S3Object)
           (java.io InputStream)))

(def credential
  "A credential object for AWS."
  (DefaultAWSCredentialsProviderChain.))

(def s3
  "Returns AmazonS3 object."
  ^AmazonS3Client (.. AmazonS3ClientBuilder
                    (standard)
                    (withRegion Regions/AP_NORTHEAST_1)
                    (withCredentials credential)
                    (build)))

(defn get-object-as-json
  "Returns S3 object, assuming and parsing it as a JSON."
  [^String bucket ^String key]
  (with-open [^S3Object object (.getObject s3 bucket key)
              ^InputStream stream (.getObjectContent object)]
    (cheshire/parse-stream (reader stream))))
