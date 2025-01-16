package com.example.mobile.orders.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GooglePredictionsDto(
    val predictions: List<GooglePredictionDto>
)

@Serializable
data class GooglePredictionDto(
    val description: String,
    @SerialName("matched_substrings")
    val matchedSubstring: List<MatchedSubstringDto>,
    val place_id: String,
    val reference: String,
    @SerialName("structured_formatting")
    val structuredFormatting: StructuredFormattingDto,
    val terms: List<TermsDto>,
    val types: List<String>
)

@Serializable
data class StructuredFormattingDto(
    @SerialName("main_text")
    val mainText: String,
    @SerialName("main_text_matched_substrings")
    val mainTextMatchedSubstrings: List<MatchedSubstringDto>,
    @SerialName("secondary_text")
    val secondaryText: String
)

@Serializable
data class MatchedSubstringDto(
    val length: Int,
    val offset: Int
)

@Serializable
data class TermsDto(
    val value: String,
    val offset: Int
)
