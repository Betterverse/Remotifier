package net.betterverse.remotifier;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VoteListener;

public class RemotifierListener implements VoteListener {
	public void voteMade(Vote vote) {
		System.out.println("Remotifier received a vote from " + vote.getUsername());
		Remotifier.Instance.DB.Insert(vote.getUsername(), vote.getAddress());
	}
}
