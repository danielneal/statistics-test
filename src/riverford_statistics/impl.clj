(ns riverford-statistics.impl
  (:require [clojure.string :as string]))

(defn whitespace-delimited-word-count
  "Takes a text string and returns the whitespace delimited word count"
  [text]
  (let [words (string/split text #"\s")]
    (count words)))

(defn line-count
  "Takes a text string and retunrs the line count"
  [text]
  (let [lines (string/split text #"\r?\n")]
    (count lines)))

(defn letters-per-word
  "Returns a seq of the letters per whitespace-delimited word in the text"
  [text]
  (let [words (string/split text #"\s")
        letters-per-word (map count words)]
    letters-per-word))

(defn mean
  "Returns the mean of the numbers"
  [numbers]
  (with-precision 5 (bigdec (/ (reduce + numbers) (count numbers)))))

(defn median
  "Returns the median of the numbers - note that the case for an even number
   of numbers must average the numbers either side of the midpoint, whereas
   for an odd number of numbers, the midpoint is taken"
  [numbers]
  (let [sorted-numbers (sort numbers)]
    (if (odd? (count numbers))
      (nth sorted-numbers (/ (count numbers) 2))
      (let [midpoint (bigdec (/ (count numbers) 2))
            above-midpoint (java.lang.Math/ceil midpoint)
            below-midpoint (dec above-midpoint)
            upper (nth sorted-numbers above-midpoint)
            lower (nth sorted-numbers below-midpoint)]
        (bigdec (/ (+ lower upper) 2))))))

(defn mode
  "Returns the mode (most frequent) of the numbers"
  [numbers]
  (first (apply max-key val (frequencies numbers))))

(defn most-common-letter
  "Returns the most common letter, given the provided text"
  [text]
  (first (apply max-key val (dissoc (frequencies text) \space))))

(defn calculate-statistics [text]
  (let [letters-per-word (letters-per-word text)]
    [{:title "Whitespace delimited word count"
      :value (whitespace-delimited-word-count text)}
     {:title "Line count"
      :value (line-count text)}
     {:title "Mean letters per word"
      :value (mean letters-per-word)}
     {:title "Median letters per word"
      :value (median letters-per-word)}
     {:title "Mode letters per word"
      :value (mode letters-per-word)}
     {:title "Most common letter"
      :value (most-common-letter text)}]))
