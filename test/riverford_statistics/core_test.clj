(ns riverford-statistics.core-test
  (:require [clojure.test :refer :all]
            [riverford-statistics.impl :refer :all]))

(def text
  "We would like you to write a small website that calculates statistics about text files that are uploaded to it and displays those statistics in a useful form.
 The site can be developed in any language and can make use ofany technologies you see fit.")

(deftest correct-word-count
  (is (= 45 (whitespace-delimited-word-count text))))

(deftest correct-line-count
  (is (= 2 (line-count text))))

(deftest correct-mean
  (is (= (mean [1 2 3 4 5]) 3.0M)))

(deftest correct-median-odd
  (is (= (median [1 2 3 4 5]) 3)))

(deftest correct-median-even
  (is (= (median [1 2 3 4]) 2.5M)))

(deftest correct-mode
  (is (= (mode [1 1 2 3 4]) 1)))
