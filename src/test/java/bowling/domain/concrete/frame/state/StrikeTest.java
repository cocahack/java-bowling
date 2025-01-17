package bowling.domain.concrete.frame.state;

import bowling.domain.RollResult;
import bowling.domain.engine.frame.Score;
import bowling.domain.engine.frame.state.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class StrikeTest {

    private State strikeState;

    @BeforeEach
    void setUp() {
        RollResult firstRoll = RollResult.of(10);
        strikeState = new Strike(firstRoll);
    }

    @Test
    @DisplayName("Strike 상태는 더 이상 다른 상태로 바뀔 수 없다.")
    void cannotTransit() {
        assertThatThrownBy(() -> strikeState.transit(RollResult.of(0)))
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("완료 상태를 확인할 때는 항상 true 를 반환한다.")
    void returnFalseIfCallIsFinished() {
        assertThat(strikeState.isFinished()).isTrue();
    }

    @Test
    @DisplayName("Strike 상태에서는 X 로 표기하며, 이를 내보낸다.")
    void export() {
        assertThat(strikeState.export()).isEqualTo("X");
    }

    @Test
    @DisplayName("핀을 전부 쓰러트리지 않은 결과로 초기화하면 예외 처리한다.")
    void throwExceptionIfSomePinsRemain() {
        assertThatThrownBy(() -> new Strike(RollResult.of(6))).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("점수를 생성한다. 생성 직후에는 다른 프레임에서 추가 점수를 가져와야 한다.")
    void createScore() {
        assertThat(strikeState.createScore().isCalculationCompleted()).isFalse();
    }

    @Test
    @DisplayName("Score 에 추가로 점수를 넣어 준다.")
    void giveScores() {
        Score strikeScore = Score.initStrikeScore();
        Score spareScore = Score.initSpareScore();

        assertAll(
            () -> assertThat(strikeState.addScoreTo(strikeScore).isCalculationCompleted()).isFalse(),
            () -> assertThat(strikeState.addScoreTo(spareScore).getValue()).isEqualTo(20)
        );
    }
    
}

