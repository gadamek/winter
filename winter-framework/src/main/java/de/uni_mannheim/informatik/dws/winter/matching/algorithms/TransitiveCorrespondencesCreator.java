/*
 * Copyright (c) 2017 Data and Web Science Group, University of Mannheim, Germany (http://dws.informatik.uni-mannheim.de/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package de.uni_mannheim.informatik.dws.winter.matching.algorithms;

import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.Pair;
import de.uni_mannheim.informatik.dws.winter.processing.DataIterator;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

/**
 * 
 * Takes a set of correspondences as input and creates all correspondences that can be inferred by transitivity
 * 
 * @author Oliver Lehmberg (oli@dwslab.de)
 *
 */
public class TransitiveCorrespondencesCreator<TypeA extends Matchable, TypeB extends Matchable> implements MatchingAlgorithm<TypeA, TypeB> {

	private Processable<Correspondence<TypeA, TypeB>> correspondences;
	private Processable<Correspondence<TypeA, TypeB>> result;

	public TransitiveCorrespondencesCreator(Processable<Correspondence<TypeA, TypeB>> correspondences) {
		this.correspondences = correspondences;
	}
	
	/* (non-Javadoc)
	 * @see de.uni_mannheim.informatik.dws.winter.matching.algorithms.MatchingAlgorithm#run()
	 */
	@Override
	public void run() {
		
		result = correspondences
					.join(correspondences, (c)->c.getSecondRecord().getIdentifier(), (c)->c.getFirstRecord().getIdentifier())
					.map(
						(Pair<Correspondence<TypeA, TypeB>, Correspondence<TypeA, TypeB>> record, DataIterator<Correspondence<TypeA, TypeB>> resultCollector)
						-> {
							TypeA left = record.getFirst().getFirstRecord();
							TypeA right = record.getSecond().getSecondRecord();
							double sim = (record.getFirst().getSimilarityScore()+record.getSecond().getSimilarityScore())/2.0;
							
							resultCollector.next(new Correspondence<TypeA, TypeB>(left, right, sim));
						}
					)
					.append(correspondences)
					.distinct();
		
	}

	/* (non-Javadoc)
	 * @see de.uni_mannheim.informatik.dws.winter.matching.algorithms.MatchingAlgorithm#getResult()
	 */
	@Override
	public Processable<Correspondence<TypeA, TypeB>> getResult() {
		return result;
	}

	
	
}