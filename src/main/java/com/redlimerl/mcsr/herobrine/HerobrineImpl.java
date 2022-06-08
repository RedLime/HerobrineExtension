package com.redlimerl.mcsr.herobrine;

import com.redlimerl.speedrunigt.api.SpeedRunIGTApi;
import com.redlimerl.speedrunigt.timer.category.RunCategory;

public class HerobrineImpl implements SpeedRunIGTApi {
    @Override
    public RunCategory registerCategory() {
        return Herobrine.HEROBRINE;
    }
}
