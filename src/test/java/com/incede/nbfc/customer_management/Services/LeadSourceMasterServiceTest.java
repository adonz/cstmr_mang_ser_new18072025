package com.incede.nbfc.customer_management.Services;

import com.incede.nbfc.customer_management.DTOs.LeadSourceMasterDto;
import com.incede.nbfc.customer_management.Exceptions.BusinessException.BusinessException;
import com.incede.nbfc.customer_management.Models.LeadSourceMaster;
import com.incede.nbfc.customer_management.Repositories.LeadSourceMasterRepository;
import com.incede.nbfc.customer_management.TenantConfig.TenantConfig;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LeadSourceMasterServiceTest {

    @Mock
    private LeadSourceMasterRepository repository;

    @Mock
    private TenantConfig tenantConfig;

    @InjectMocks
    private LeadSourceMasterService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(tenantConfig.getTenantId()).thenReturn(101);
    }

    @Test
    void testCreateLeadSource_Success() {
        LeadSourceMasterDto dto = new LeadSourceMasterDto();
        dto.setSourceName("Online");
        dto.setCreatedBy(1);

        when(repository.existsByTenantIdAndSourceNameIgnoreCaseAndIsDeleteFalse(101, "Online")).thenReturn(false);
        when(repository.save(any(LeadSourceMaster.class))).thenAnswer(invocation -> invocation.getArgument(0));

        LeadSourceMasterDto result = service.create(dto);

        assertEquals("Online", result.getSourceName());
        assertNotNull(result.getCreatedAt());
    }

    @Test
    void testCreateLeadSource_ThrowsOnDuplicate() {
        LeadSourceMasterDto dto = new LeadSourceMasterDto();
        dto.setSourceName("Online");
        dto.setCreatedBy(1);

        when(repository.existsByTenantIdAndSourceNameIgnoreCaseAndIsDeleteFalse(101, "Online")).thenReturn(true);

        assertThrows(BusinessException.class, () -> service.create(dto));
    }

    @Test
    void testUpdateLeadSource_Success() {
        LeadSourceMaster existing = new LeadSourceMaster();
        existing.setSourceId(1);
        existing.setSourceName("Old");
        existing.setIsDelete(false);

        when(repository.findBySourceIdAndIsDeleteFalse(1)).thenReturn(Optional.of(existing));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        LeadSourceMasterDto updateDto = new LeadSourceMasterDto();
        updateDto.setSourceName("Updated");
        updateDto.setUpdatedBy(99);

        LeadSourceMasterDto result = service.update(1, updateDto);

        assertEquals("Updated", result.getSourceName());
        assertEquals(99, result.getUpdatedBy());
    }

    @Test
    void testUpdateLeadSource_NotFound() {
        when(repository.findBySourceIdAndIsDeleteFalse(2)).thenReturn(Optional.empty());

        LeadSourceMasterDto dto = new LeadSourceMasterDto();
        dto.setSourceName("New");

        assertThrows(BusinessException.class, () -> service.update(2, dto));
    }

    @Test
    void testSoftDelete_Success() {
        LeadSourceMaster entity = new LeadSourceMaster();
        entity.setSourceId(1);
        entity.setIsDelete(false);

        when(repository.findBySourceIdAndIsDeleteFalse(1)).thenReturn(Optional.of(entity));

        service.softDelete(1, 55);

        assertTrue(entity.getIsDelete());
        assertEquals(55, entity.getUpdatedBy());
    }

    @Test
    void testGetAllByTenant() {
        LeadSourceMaster lead1 = new LeadSourceMaster();
        lead1.setSourceName("Referral");

        when(repository.findAllByTenantIdAndIsDeleteFalse(101)).thenReturn(List.of(lead1));

        List<LeadSourceMasterDto> result = service.getAllByTenant();

        assertEquals(1, result.size());
        assertEquals("Referral", result.get(0).getSourceName());
    }

    @Test
    void testGetByIdentity_Success() {
        UUID uuid = UUID.randomUUID();
        LeadSourceMaster lead = new LeadSourceMaster();
        lead.setIdentity(uuid);
        lead.setSourceName("Agent");

        when(repository.findByIdentityAndIsDeleteFalse(uuid)).thenReturn(Optional.of(lead));

        LeadSourceMasterDto result = service.getByIdentity(uuid);

        assertEquals("Agent", result.getSourceName());
    }

    @Test
    void testGetByIdentity_NotFound() {
        UUID uuid = UUID.randomUUID();

        when(repository.findByIdentityAndIsDeleteFalse(uuid)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> service.getByIdentity(uuid));
    }
}
