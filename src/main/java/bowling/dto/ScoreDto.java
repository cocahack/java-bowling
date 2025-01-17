package bowling.dto;

import bowling.domain.engine.frame.Score;

public class ScoreDto {

    private final String score;

    private ScoreDto(String score) {
        this.score = score;
    }

    public static ScoreDto of(Score score) {
        if (score.isUnavailable()) {
            return ScoreDto.empty();
        }
        return new ScoreDto(String.valueOf(score.getValue()));
    }

    public static ScoreDto empty() {
        return new ScoreDto("");
    }

    public String getScore() {
        return score;
    }
}
