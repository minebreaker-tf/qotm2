(ns qotm2.get_quote
  (:require [cheshire.core :as cheshire]
            [clojure.java.io :refer [reader writer]]
            [qotm2.aws :as aws])
  (:import (com.amazonaws.services.lambda.runtime RequestStreamHandler Context)
           (java.io InputStream)
           (java.io OutputStream))
  (:gen-class :implements [RequestStreamHandler]))

(def bucket "The name of the S3 bucket to use." (System/getenv "QOTM_S3_BUCKET_NAME"))
(def index-file "The index file name of the quotes." "index")

(defn get-quote [params]
  "Get a quote from S3."
  ; 1. S3からインデックスファイルを取得
  ; 2. インデックスからS3IDをランダムにピック
  ; 3. そのS3オブジェクトを取得
  ; 4. 返す
  (aws/get-object-as-json
    bucket
    (rand-nth (aws/get-object-as-json bucket index-file))))

(defn -handleRequest [^RequestStreamHandler this ^InputStream is ^OutputStream os ^Context context]
  (->
    (cheshire/parse-stream (reader is))
    (get-quote)
    (cheshire/generate-stream (writer os))))
