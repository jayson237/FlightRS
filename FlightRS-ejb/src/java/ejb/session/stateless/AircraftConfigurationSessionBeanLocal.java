/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.AircraftConfiguration;
import entity.CabinClass;
import java.util.List;
import javax.ejb.Local;
import util.exception.AircraftConfigurationExistException;
import util.exception.AircraftConfigurationNotFoundException;
import util.exception.AircraftTypeNotFoundException;
import util.exception.CabinClassExistException;
import util.exception.ConfigurationExceedCapacity;
import util.exception.GeneralException;
import util.exception.InputDataValidationException;

/**
 *
 * @author jayso
 */
@Local
public interface AircraftConfigurationSessionBeanLocal {

    public AircraftConfiguration createNewAircraftConfig(AircraftConfiguration config, List<CabinClass> cabinClasses) throws ConfigurationExceedCapacity, CabinClassExistException, AircraftTypeNotFoundException, AircraftConfigurationExistException, GeneralException, InputDataValidationException;

    public List<AircraftConfiguration> retrieveAllAirConfigurations();

    public AircraftConfiguration retrieveAirConfigById(Long aircraftConfigId) throws AircraftConfigurationNotFoundException;

    public AircraftConfiguration retrieveAirConfigByName(String aircraftConfig) throws AircraftConfigurationNotFoundException;

}
