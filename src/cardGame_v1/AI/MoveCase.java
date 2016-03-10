package cardGame_v1.AI;

public enum MoveCase {
	PlayCard(0,PlayOutcome.NA),
	AttackPlayer(2, PlayOutcome.H, PlayOutcome.M),
	AttackCard(4, PlayOutcome.HH, PlayOutcome.HM, PlayOutcome.MH, PlayOutcome.MM);
	
	int chanceNodes;
	PlayOutcome[] playOutcomes;
	
	MoveCase(int chanceNodes, PlayOutcome playOutcome1, PlayOutcome playOutcome2, PlayOutcome playOutcome3, PlayOutcome playOutcome4) {
		this.chanceNodes = chanceNodes;
		playOutcomes = new PlayOutcome[chanceNodes];
	}
	MoveCase(int chanceNodes, PlayOutcome playOutcome1, PlayOutcome playOutcome2) {
		this.chanceNodes = chanceNodes;
	}
	MoveCase(int chanceNodes, PlayOutcome playOutcome) {
		this.chanceNodes = chanceNodes;
	}
}
