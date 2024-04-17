package com.bbs.service;

import com.bbs.entity.Configuration;
import com.bbs.repository.ConfigurationRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ConfigurationService {
    @Resource
    private ConfigurationRepository configurationRepository;

    public boolean getReviewSwitch() {
        return configurationRepository.findById("reviewSwitch")
                .map(Configuration::getValue)
                .map(Boolean::parseBoolean)
                .orElse(false);
    }

    public void setReviewSwitch(boolean enabled) {
        Configuration reviewSwitch = configurationRepository.findById("reviewSwitch")
                .orElse(new Configuration());
        reviewSwitch.setSetting("reviewSwitch");
        reviewSwitch.setValue(Boolean.toString(enabled));
        configurationRepository.save(reviewSwitch);
    }
}
