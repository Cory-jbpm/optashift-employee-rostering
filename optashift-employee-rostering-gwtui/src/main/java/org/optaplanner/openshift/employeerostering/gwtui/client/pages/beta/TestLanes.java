/*
 * Copyright (C) 2018 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.optaplanner.openshift.employeerostering.gwtui.client.pages.beta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.Dependent;

import org.optaplanner.openshift.employeerostering.gwtui.client.beta.java.model.Blob;
import org.optaplanner.openshift.employeerostering.gwtui.client.beta.java.model.Lane;
import org.optaplanner.openshift.employeerostering.gwtui.client.beta.java.model.SubLane;
import org.optaplanner.openshift.employeerostering.gwtui.client.beta.java.test.TestBlob;

@Dependent
public class TestLanes {

    private static final int DAYS = 7;

    public List<Lane> getAll() {

        final Lane emergencyRoom = new Lane("Emergency Room", Arrays.asList(
                new SubLane(repeatFor(DAYS, 4, 0, 0, "Early", "Late", "Night")),
                new SubLane(repeatFor(DAYS, 4, 8, 2, "Day"))
        ));

        final Lane intenseCare = new Lane("Intense Care", Arrays.asList(
                new SubLane(repeatFor(DAYS, 4, 0, 0, "Early", "Late", "Night")),
                new SubLane(repeatFor(DAYS, 4, 4, 2, "Day", "Afternoon"))
        ));

        return Arrays.asList(
                emergencyRoom,
                intenseCare,
                emergencyRoom,
                intenseCare,
                emergencyRoom,
                intenseCare,
                emergencyRoom,
                intenseCare);
    }

    private List<Blob> repeatFor(final int times,
                                 final int size,
                                 final int positionIncrement,
                                 final int initialPosition,
                                 final String... labels) {

        int currentPosition = initialPosition;

        final List<Blob> ret = new ArrayList<>();

        for (int i = 0; i < times; i++) {
            for (int j = 0; j < labels.length; j++) {
                ret.add(new TestBlob(labels[j], size, currentPosition, false));
                currentPosition += size;
            }
            currentPosition += positionIncrement;
        }

        return ret;
    }
}