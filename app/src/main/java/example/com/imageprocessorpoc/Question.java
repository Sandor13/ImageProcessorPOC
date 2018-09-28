package example.com.imageprocessorpoc;

import com.google.gson.annotations.SerializedName;

/**
 * @author Aleksandr Anikin
 */
public final class Question {

    @SerializedName("questionId")
    private final String questionId;
    @SerializedName("proQuestion")
    private final String proQuestion;
    @SerializedName("layQuestion")
    private final String layQuestion;
    @SerializedName("type")
    private final String type;
    @SerializedName("multiple")
    private final Boolean multiple;
    @SerializedName("codeSystem")
    private final String codeSystem;
    @SerializedName("code")
    private final String code;
    @SerializedName("unit")
    private final String[] unit;
    @SerializedName("lowValue")
    private final Double[] lowValue;
    @SerializedName("highValue")
    private final Double[] highValue;
    @SerializedName("maxDec")
    private final Integer maxDec;

    public Question(String questionId, String proQuestion, String layQuestion, String type, Boolean multiple, String codeSystem, String code, String[] unit, Double[] lowValue, Double[] highValue, Integer maxDec) {
        this.questionId = questionId;
        this.proQuestion = proQuestion;
        this.layQuestion = layQuestion;
        this.type = type;
        this.multiple = multiple;
        this.codeSystem = codeSystem;
        this.code = code;
        this.unit = unit;
        this.lowValue = lowValue;
        this.highValue = highValue;
        this.maxDec = maxDec;
    }

    public String getQuestionId() {
        return questionId;
    }

    public String getProQuestion() {
        return proQuestion;
    }

    public String getLayQuestion() {
        return layQuestion;
    }

    public String getType() {
        return type;
    }

    public Boolean isMultiple() {
        return multiple;
    }

    public String getCodeSystem() {
        return codeSystem;
    }

    public String getCode() {
        return code;
    }

    public String[] getUnit() {
        return unit;
    }

    public Double[] getLowValue() {
        return lowValue;
    }

    public Double[] getHighValue() {
        return highValue;
    }

    public Integer getMaxDec() {
        return maxDec;
    }
}
