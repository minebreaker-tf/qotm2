(ns qotm2.get_quote
  (require [cheshire.core :as cheshire]
           [clojure.java.io :refer :all])
  (import (com.amazonaws.services.lambda.runtime RequestStreamHandler Context)
          (java.io InputStream)
          (java.io OutputStream))
  (:gen-class :implements [RequestStreamHandler]))

; 1. S3からインデックスファイルを取得
; 2. インデックスからS3IDをランダムにピック
; 3. そのS3オブジェクトを取得
; 4. 返す
(defn get-quote [params]
  {"result" "hello, clojure"})

(defn -handleRequest [^RequestStreamHandler this ^InputStream is ^OutputStream os ^Context context]
  (->
    (cheshire/parse-stream (reader is))
    (get-quote)
    (cheshire/generate-stream (writer os))))
