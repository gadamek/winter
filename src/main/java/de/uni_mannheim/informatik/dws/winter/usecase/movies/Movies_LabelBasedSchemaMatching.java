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
package de.uni_mannheim.informatik.dws.winter.usecase.movies;

import java.io.File;

import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.DataSet;
import de.uni_mannheim.informatik.dws.winter.model.HashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.CSVRecordReader;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Record;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.comparators.LabelComparatorJaccard;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

/**
 * 
 * Example of a basic setup for feature-based schema matching 
 * 
 * @author Oliver Lehmberg (oli@dwslab.de)
 *
 */
public class Movies_LabelBasedSchemaMatching {

	public static void main(String[] args) throws Exception {
		
		// load data
		DataSet<Record, Attribute> data1 = new HashedDataSet<>();
		new CSVRecordReader(0).loadFromCSV(new File("usecase/movie/input/scifi1.csv"), data1);
		DataSet<Record, Attribute> data2 = new HashedDataSet<>();
		new CSVRecordReader(0).loadFromCSV(new File("usecase/movie/input/scifi2.csv"), data2);
		
		// Initialize Matching Engine
		MatchingEngine<Record, Attribute> engine = new MatchingEngine<>();

		Processable<Correspondence<Attribute, Record>> correspondences = engine.runLabelBasedSchemaMatching(data1.getSchema(), data2.getSchema(), new LabelComparatorJaccard(), 0.5);
		
		// print results
		for(Correspondence<Attribute, Record> cor : correspondences.get()) {
			System.out.println(String.format("'%s' <-> '%s' (%.4f)", cor.getFirstRecord().getName(), cor.getSecondRecord().getName(), cor.getSimilarityScore()));
		}
	}
	
}