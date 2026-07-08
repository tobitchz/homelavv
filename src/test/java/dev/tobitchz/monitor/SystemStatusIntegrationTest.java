package dev.tobitchz.monitor;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class SystemStatusIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void distributedSystemStatusEndpointShouldExposeClusterOverview() throws Exception {
        mockMvc.perform(get("/api/system"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clusterName").value("homelab-monitor"))
                .andExpect(jsonPath("$.components['mqtt-broker']").value("online"))
                .andExpect(jsonPath("$.components['api-server']").value("online"))
                .andExpect(jsonPath("$.components['frontend']").value("online"));
    }
}
