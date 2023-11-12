/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.AircraftConfiguration;
import entity.CabinClass;
import java.util.List;
import javax.ejb.Remote;
import util.exception.AircraftConfigurationExistException;
import util.exception.AircraftConfigurationNotFoundException;
import util.exception.AircraftTypeNotFoundException;
import util.exception.GeneralException;
import util.exception.InputDataValidationException;

/**
 *
 * @author jayso
 */
@Remote
public interface AircraftConfigurationSessionBeanRemote {

    public AircraftConfiguration createNewAircraftConfig(String aircraftType, AircraftConfiguration config, List<CabinClass> cabinClasses) throws AircraftTypeNotFoundException, AircraftConfigurationExistException, GeneralException, InputDataValidationException;

    public List<AircraftConfiguration> retrieveAllAirConfigurations();

    public AircraftConfiguration retrieveAirConfigById(Long aircraftConfigId) throws AircraftConfigurationNotFoundException;

}
