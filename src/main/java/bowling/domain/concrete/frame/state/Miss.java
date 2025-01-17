package bowling.domain.concrete.frame.state;

import bowling.domain.RollResult;
import bowling.domain.engine.frame.Score;
import bowling.domain.engine.frame.state.Finished;
import bowling.dto.RollResultsDto;
import bowling.dto.StateExporter;

public class Miss extends Finished {

    private final RollResult firstRollResult;
    private final RollResult secondRollResult;

    public Miss(RollResult firstRollResult, RollResult secondRollResult) {
        validate(firstRollResult, secondRollResult);
        this.firstRollResult = firstRollResult;
        this.secondRollResult = secondRollResult;
    }

    private void validate(RollResult firstRoll, RollResult secondRoll) {
        validateIfFirstRollIsStrike(firstRoll);
        validateTwoResultsNotClearFrame(firstRoll, secondRoll);
    }

    private void validateIfFirstRollIsStrike(RollResult firstRoll) {
        if (firstRoll.isClear()) {
            throw new IllegalArgumentException("첫 번째 투구가 스트라이크면 Miss 상태를 만들 수 없습니다.");
        }
    }

    private void validateTwoResultsNotClearFrame(RollResult firstRoll, RollResult secondRoll) {
        if (firstRoll.isClearWith(secondRoll)) {
            throw new IllegalArgumentException("두 번째 투구에서 모든 핀이 쓰러졌다면 Miss 상태가 아닙니다.");
        }
    }

    @Override
    public String export() {
        return StateExporter.MISS.export(RollResultsDto.of(firstRollResult, secondRollResult));
    }

    @Override
    public Score addScoreTo(Score score) {
        Score completedScore = score.add(firstRollResult);

        if (!completedScore.isCalculationCompleted()) {
            completedScore = completedScore.add(secondRollResult);
        }

        return completedScore;
    }

    @Override
    public Score createScore() {
        return Score.initReadyToUseScore(getKnockedDownPins());
    }

    private int getKnockedDownPins() {
        return firstRollResult.getValue() + secondRollResult.getValue();
    }

}
