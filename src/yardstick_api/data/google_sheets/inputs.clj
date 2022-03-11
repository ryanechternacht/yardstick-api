(ns yardstick-api.data.google-sheets.inputs)

(def single-db-pull {:school-id 1
                     :students [{:id "abc123"
                                 :state-id "oh-abc123"}
                                {:id "abc124"
                                 :state-id "oh-abc124"}
                                {:id "abc125"
                                 :state-id ""}
                                {:id ""
                                 :state-id "oh-abc126"}]
                     :source {:display "MAP - Mathematics"
                              :table "assessment_map_v1"}
                     :metrics [{:display "RIT Score"
                                :column "testritscore"}
                               {:display "Test Duration"
                                :column "testdurationminutes"}
                               {:display "Rapid Guessing %"
                                :column "rapidguessingpercentage"}]
                     :periods [{:year 2020
                                :period-id 1}
                               {:year 2020
                                :period-id 2}
                               {:year 2020
                                :period-id 3}
                               {:year 2021
                                :period-id 1}
                               {:year 2021
                                :period-id 1}
                               {:year 2021
                                :period-id 1}]})

(def from-front-end {:school-id 1
                     :sources [{:source {:display "MAP - Mathematics"
                                         :table "assessment_map_v1"}
                                :periods [{:year 2020
                                           :period-id 1}
                                          {:year 2020
                                           :period-id 2}
                                          {:year 2020
                                           :period-id 3}
                                          {:year 2021
                                           :period-id 1}
                                          {:year 2021
                                           :period-id 1}
                                          {:year 2021
                                           :period-id 1}]
                                :metrics [{:display "RIT Score"
                                           :column "assessment_map_v1.testritscore"}
                                          {:display "Test Duration"
                                           :column "assessment_map_v1.testdurationminutes"}
                                          {:display "Rapid Guessing %"
                                           :column "assessment_map_v1.rapidguessingpercentage"}]}
                               {:source {:display "MAP - ELA"
                                         :table "assessment_ela_v1"}
                                :periods [{:year 2020
                                           :period-id 1}
                                          {:year 2020
                                           :period-id 2}
                                          {:year 2020
                                           :period-id 3}
                                          {:year 2021
                                           :period-id 1}
                                          {:year 2021
                                           :period-id 2}
                                          {:year 2021
                                           :period-id 3}]
                                :metrics [{:display "RIT Score"
                                           :column "testritscore"}
                                          {:display "Test Duration"
                                           :column "testdurationminutes"}
                                          {:display "Rapid Guessing %"
                                           :column "rapidguessingpercentage"}]}]
                     :students :nil ;; optional filtering
                     })

