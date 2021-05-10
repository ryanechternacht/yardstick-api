(ns yardstick-api.data.layout
  (:require [honeysql.helpers :refer [select from merge-join merge-where order-by]]
            [yardstick-api.db :as db]
            [yardstick-api.data.language :as lang]))

; TODO should this be broken into sections?

(def ^:private layout-keys
  {:layout_obstacles_and_opportunities_lang "layout-obstacles-and-opportunities"
   :layout_home_lang "layout-home"
   :layout_how_to_help_lang "layout-how-to-help"
   :layout_assessment_lang "layout-assessment"
   :layout_opportunities_lang "layout-opportunities"
   :layout_opportunities_cta_lang "layout-opportunities-cta"
   :layout_obstacles_lang "layout-obstacles"
   :layout_obstacles_line_1_lang "layout-obstacles-line-1"
   :layout_obstacles_line_2_lang "layout-obstacles-line-2"
   :layout_obstacles_line_3_lang "layout-obstacles-line-3"
   :layout_obstacles_cta_lang "layout-obstacles-cta"
   :assessment_off_track_lang "assessment-off-track"
   :assessment_at_risk_lang "assessment-at-risk"
   :assessment_likely_on_track_lang "assessment-likely-on-track"
   :assessment_on_track_lang "assessment-on-track"
   :assessment_advanced_lang "assessment-advanced"
   :assessment_general_lang "assessment-general"
   :assessment_math_lang "assessment-math"
   :assessment_ela_lang "assessment-ela"
   :assessment_overview_line_1_lang "assessment-overview-line-1"
   :layout_overview_lang "layout-overview"
   :layout_breakdown_lang "layout-breakdown"
   :layout_details_lang "layout-details"
   :home_pre_headline_lang "home-pre-headline"
   :home_headline_lang "home-headline"
   :home_nav_lang "home-nav"
   :home_nav_how_to_help_lang "home-nav-how-to-help"
   :home_nav_assessment_lang "home-nav-assessment"
   :home_nav_obstacles_and_opportunities_lang "home-nav-obstacles-and-opportunities"})

(defn keys->obj [{:keys [layout_obstacles_and_opportunities_lang layout_home_lang
                         layout_how_to_help_lang layout_assessment_lang layout_opportunities_lang
                         layout_opportunities_cta_lang layout_obstacles_lang
                         layout_obstacles_line_1_lang layout_obstacles_line_2_lang
                         layout_obstacles_line_3_lang layout_obstacles_cta_lang
                         assessment_off_track_lang assessment_at_risk_lang
                         assessment_likely_on_track_lang assessment_on_track_lang
                         assessment_advanced_lang assessment_general_lang assessment_math_lang
                         assessment_ela_lang assessment_overview_line_1_lang layout_overview_lang
                         layout_breakdown_lang layout_details_lang home_pre_headline_lang
                         home_headline_lang home_nav_lang home_nav_how_to_help_lang
                         home_nav_assessment_lang home_nav_obstacles_and_opportunities_lang]}]
  {:layout {:obstaclesAndOpportunities layout_obstacles_and_opportunities_lang
            :home layout_home_lang
            :howToHelp layout_how_to_help_lang
            :assessment layout_assessment_lang
            :overview layout_overview_lang
            :breakdown layout_breakdown_lang
            :details layout_details_lang}
   :obstaclesAndOpportunities {:opportunities layout_opportunities_lang
                               :opportunitiesCta layout_opportunities_cta_lang
                               :obstacles layout_obstacles_lang
                               :obstaclesLine1 layout_obstacles_line_1_lang
                               :obstaclesLine2 layout_obstacles_line_2_lang
                               :obstaclesLine3 layout_obstacles_line_3_lang
                               :obstaclesCta layout_obstacles_cta_lang}
   :assessment {:offTrack assessment_off_track_lang
                :atRisk assessment_at_risk_lang
                :likelyOnTrack assessment_likely_on_track_lang
                :onTrack assessment_on_track_lang
                :advanced assessment_advanced_lang
                :general assessment_general_lang
                :math assessment_math_lang
                :ela assessment_ela_lang
                :overviewLine1 assessment_overview_line_1_lang}
   :home {:preHeadline home_pre_headline_lang
          :headline home_headline_lang
          :nav home_nav_lang
          :navHowToHelp home_nav_how_to_help_lang
          :navAssessment home_nav_assessment_lang
          :navObstaclesAndOpportunities home_nav_obstacles_and_opportunities_lang}})

(defn get-layout [db]
  (->> layout-keys
       (lang/render-language db "en")
      keys->obj))