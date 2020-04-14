package bowling;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static bowling.Pins.MAX_PIN_COUNT;
import static bowling.Pins.MIN_PIN_COUNT;

public enum FrameScoreResult {
    STRIKE(score -> "X"),
    SPARE(score -> "/"),
    MISS(Score::toString),
    GUTTER(score -> "-");

    private static final int FIRST_SCORE_INDEX = 0;
    private static final int SECOND_SCORE_INDEX = 1;

    private final Function<Score, String> printFunction;

    FrameScoreResult(final Function<Score, String> printFunction) {
        this.printFunction = printFunction;
    }

    public static FrameScoreResult of(final List<Score> scores) {
        if (scores.size() >= 2) {
            return of(scores.get(FIRST_SCORE_INDEX), scores.get(SECOND_SCORE_INDEX));
        }

        if (scores.size() == 1) {
            return of(null, scores.get(FIRST_SCORE_INDEX));
        }

        return GUTTER;
    }

    public static FrameScoreResult of(final Score preScore, final Score nowScore) {

        if (isSpare(preScore, nowScore)) {
            return SPARE;
        }

        if (isStrike(nowScore)) {
            return STRIKE;
        }

        if (isGutter(nowScore)) {
            return GUTTER;
        }

        return MISS;
    }

    public String toConsoleResult(final Score score) {
        return printFunction.apply(score);
    }

    private static boolean isSpare(final Score preScore, final Score nowScore) {
        return (!Objects.isNull(preScore) && !preScore.isEqualsTo(MIN_PIN_COUNT)) && Score.sum(Arrays.asList(preScore, nowScore)) == MAX_PIN_COUNT;
    }

    private static boolean isStrike(final Score score) {
        return score.isEqualsTo(MAX_PIN_COUNT);
    }

    private static boolean isGutter(final Score score) {
        return score.isEqualsTo(MIN_PIN_COUNT);
    }
}