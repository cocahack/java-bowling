package bowling.domain.concrete.frame.state;

import bowling.domain.RollResult;
import bowling.domain.engine.frame.Score;
import bowling.domain.engine.frame.state.Finished;
import bowling.dto.RollResultsDto;
import bowling.dto.StateExporter;

public class Spare extends Finished {

    private final RollResult firstRollResult;
    private final RollResult secondRollResult;

    public Spare(RollResult firstRollResult, RollResult secondRollResult) {
        validate(firstRollResult, secondRollResult);
        this.firstRollResult = firstRollResult;
        this.secondRollResult = secondRollResult;
    }

    private void validate(RollResult firstRoll, RollResult secondRoll) {
        validateIfFirstRollIsStrike(firstRoll);
        validateTwoResultsClearFrame(firstRoll, secondRoll);
    }

    private void validateIfFirstRollIsStrike(RollResult firstRoll) {
        if (firstRoll.isClear()) {
            throw new IllegalArgumentException("첫 번째 투구가 스트라이크면 Spare 상태가 아닙니다.");
        }
    }

    private void validateTwoResultsClearFrame(RollResult firstRoll, RollResult secondRoll) {
        if (!firstRoll.isClearWith(secondRoll)) {
            throw new IllegalArgumentException("두 번째 투구까지 모든 핀을 쓰러트리지 못했다면 Spare 상태가 아닙니다.");
        }
    }

    @Override
    public boolean canPromoteToBonusState() {
        return true;
    }

    @Override
    public String export() {
        return StateExporter.SPARE.export(RollResultsDto.of(firstRollResult, secondRollResult));
    }

    @Override
    public Score createScore() {
        return Score.initSpareScore();
    }

    @Override
    public Score addScoreTo(Score score) {
        Score completedScore = score.add(firstRollResult);

        if (!completedScore.isCalculationCompleted()) {
            completedScore = completedScore.add(secondRollResult);
        }

        return completedScore;
    }
}
