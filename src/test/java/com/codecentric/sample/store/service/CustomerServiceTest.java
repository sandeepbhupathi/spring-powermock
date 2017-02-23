package com.codecentric.sample.store.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.codecentric.sample.store.model.Customer;
import com.codecentric.sample.store.service.external.AddressService;
import com.codecentric.sample.store.service.external.HostService;

public class CustomerServiceTest {

    @Spy
    private AddressService addressService;

    @Mock
    private HostService hostService;

    @InjectMocks
    private CustomerService customerService;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testPLZAddressCombination() {

        //
        // Given
        //
        Customer customer = new Customer("204", "John Do", "221B Bakerstreet");
        when(addressService.getPLZForCustomer(customer)).thenReturn(47891);

        //
        // When
        //
        String address = customerService.getPLZAddressCombination(customer);

        //
        // Then
        //
        assertThat(address, is("47891_221B Bakerstreet"));
    }


    @Test
    public void testPLZAddressCombinationIncludingHostValue() {

        //
        // Given
        //
        Customer customer = new Customer("204", "John Do", "224B Bakerstreet");

        doAnswer(new Answer<Customer>() {
            @Override
            public Customer answer(InvocationOnMock invocation) throws Throwable {
                Object originalArgument = (invocation.getArguments())[0];
                Customer returnedValue = (Customer) originalArgument;
                returnedValue.setHostValue("TestHostValue");
                return null ;
            }
        }).when(hostService).expand(any(Customer.class));

        when(addressService.getPLZForCustomer(customer)).thenReturn(47891);
        doNothing().when(addressService).updateExternalSystems(customer);

        //
        // When
        //
        String address = customerService.getPLZAddressCombinationIncludingHostValue(customer, true);

        //
        // Then
        //
        Mockito.verify(addressService, times(1)).updateExternalSystems(any(Customer.class));
        assertThat(address, is("47891_224B Bakerstreet_TestHostValue"));
    }
}



