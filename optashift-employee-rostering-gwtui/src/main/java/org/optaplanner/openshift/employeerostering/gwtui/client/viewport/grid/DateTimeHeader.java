package org.optaplanner.openshift.employeerostering.gwtui.client.viewport.grid;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import elemental2.dom.HTMLElement;
import org.jboss.errai.ui.client.local.api.elemental2.IsElement;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.optaplanner.openshift.employeerostering.gwtui.client.viewport.CSSGlobalStyle;
import org.optaplanner.openshift.employeerostering.gwtui.client.viewport.CSSGlobalStyle.GridVariables;

@Templated
public class DateTimeHeader implements IsElement {

    @Inject
    @DataField("corner")
    @Named("span")
    private HTMLElement corner;

    @Inject
    @DataField("container")
    @Named("span")
    private HTMLElement container;

    @Inject
    @Named("span")
    private HTMLElement spanFactory;

    @Inject
    private CSSGlobalStyle cssGlobalStyle;

    private HTMLElement dateTickFactory;
    private HTMLElement dateChangeTickFactory;
    private HTMLElement timeTickFactory;

    @PostConstruct
    public void init() {
        dateTickFactory = (HTMLElement) spanFactory.cloneNode(false);
        dateChangeTickFactory = (HTMLElement) spanFactory.cloneNode(false);
        timeTickFactory = (HTMLElement) spanFactory.cloneNode(false);
        dateTickFactory.classList.add("date-tick");
        dateChangeTickFactory.classList.add("date-change-tick");
        timeTickFactory.classList.add("time-tick");
    }

    public void generateTicks(GridObjectPlacer gridObjectPlacer,
                              LinearScale<LocalDateTime> scale,
                              Long offset,
                              Function<LocalDateTime, String> dateTextSupplier,
                              Function<LocalDateTime, String> timeTextSupplier,
                              Function<LocalDateTime, List<String>> classListSupplier) {
        while (container.lastElementChild != null) {
            container.lastElementChild.remove();
        }

        long timeStep = cssGlobalStyle.getGridVariableValue(GridVariables.GRID_SOFT_LINE_INTERVAL).longValue();
        long dateStep = cssGlobalStyle.getGridVariableValue(GridVariables.GRID_HARD_LINE_INTERVAL).longValue();
        for (long i = offset - dateStep; i < scale.getEndInGridPixels() - scale.getStartInGridPixels(); i += timeStep) {
            if (!GridObjectPlacer.isHidden(i, i, scale) && (i + offset) % dateStep != 0) {
                HTMLElement timeTick = (HTMLElement) timeTickFactory.cloneNode(false);
                gridObjectPlacer.setStartPositionInGridUnits(timeTick, scale, i, true);
                gridObjectPlacer.setEndPositionInGridUnits(timeTick, scale, i + timeStep, true);
                timeTick.innerHTML = timeTextSupplier.apply(scale.toScaleUnits(i + offset));
                container.appendChild(timeTick);
            }

            if (!GridObjectPlacer.isHidden(i, i, scale) && (i + offset) % dateStep == 0) {
                HTMLElement dateTick = (HTMLElement) dateTickFactory.cloneNode(false);
                HTMLElement dateChangeTick = (HTMLElement) dateChangeTickFactory.cloneNode(false);
                gridObjectPlacer.setStartPositionInGridUnits(dateChangeTick, scale, i, true);
                gridObjectPlacer.setEndPositionInGridUnits(dateChangeTick, scale, i, true);
                gridObjectPlacer.setStartPositionInGridUnits(dateTick, scale, i, true);
                gridObjectPlacer.setEndPositionInGridUnits(dateTick, scale, i + dateStep, true);
                dateTick.innerHTML = dateTextSupplier.apply(scale.toScaleUnits(i + offset));
                classListSupplier.apply(scale.toScaleUnits(i)).forEach((clazz) -> dateTick.classList.add(clazz));
                container.appendChild(dateTick);
                container.appendChild(dateChangeTick);
            }
        }
    }
}
