package com.collins.backend.services.appointments;

import com.collins.backend.dtos.AppointmentDto;
import com.collins.backend.dtos.EntityConverter;
import com.collins.backend.dtos.PetDto;
import com.collins.backend.entities.Appointment;
import com.collins.backend.entities.Pet;
import com.collins.backend.entities.User;
import com.collins.backend.entities.enums.AppointmentStatus;
import com.collins.backend.exceptions.ResourceNotFoundException;
import com.collins.backend.payloads.requests.AppointmentUpdateRequest;
import com.collins.backend.payloads.requests.BookAppointmentRequest;
import com.collins.backend.repositories.AppointmentRepository;
import com.collins.backend.repositories.UserRepository;
import com.collins.backend.services.pets.PetService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.collins.backend.utils.FeedBackMessages.*;


@Service
@RequiredArgsConstructor
public class AppointmentService implements AppointmentServiceImpl{

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final PetService petService;
    private final EntityConverter<Appointment, AppointmentDto> entityConverter;
    private final EntityConverter<Pet, PetDto> petEntityConverter;
    private final ModelMapper modelMapper;

    @Transactional
    @Override
    public Appointment createAppointment(BookAppointmentRequest request, Long senderId, Long recipientId)  {
        Optional<User> sender = userRepository.findById(senderId);
        Optional<User> recipient = userRepository.findById(recipientId);
        if(sender.isPresent() && recipient.isPresent()) {

            Appointment appointment = request.getAppointment();
            List<Pet> pets = request.getPets();
;            pets.forEach(pet -> pet.setAppointment(appointment));
            List<Pet> savedPets = petService.savePetsForAppointment(pets);
            appointment.setPets(savedPets);

            appointment.addPatient(sender.get());
            appointment.addVeterinarian(recipient.get());
            appointment.setAppointmentNo();
            appointment.setStatus(AppointmentStatus.WAITING_FOR_APPROVAL);
            return appointmentRepository.save(appointment);
        }
        throw new ResourceNotFoundException(SENDER_RECIPIENT_NOT_FOUND);
    }

    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    @Override
    public Appointment updateAppointment(Long id, AppointmentUpdateRequest request) {
        Appointment existingAppointment = getAppointmentById(id);
        //if this patient is not waiting for approval then throw a message to a user
        if(!Objects.equals(existingAppointment.getStatus(), AppointmentStatus.WAITING_FOR_APPROVAL)) {
            throw new IllegalStateException(ALREADY_APPROVED);
        }
        existingAppointment.setAppointmentDate(LocalDate.parse(request.getAppointmentDate()));
        existingAppointment.setAppointmentTime(LocalTime.parse(request.getAppointmentTime()));
        existingAppointment.setReason(request.getReason());
        return appointmentRepository.save(existingAppointment);
    }

    @Override
    public void deleteAppointment(Long id) {
        appointmentRepository.findById(id)
               .ifPresentOrElse(appointmentRepository::delete, () -> {
                    throw new ResourceNotFoundException(RESOURCE_NOT_FOUND);
                });
    }

    @Override
    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NOT_FOUND));
    }

    @Override
    public Appointment getAppointmentByNo(String appointmentNo) {
        return appointmentRepository.findByAppointmentNo(appointmentNo);
    }

    @Override
    public List<AppointmentDto> getUserAppointments(Long userId) {
        List<Appointment> appointments = appointmentRepository.findAllByUserId(userId);
        return appointments.stream().map(appointment -> {
                    AppointmentDto appointmentDto = entityConverter.mapEntityToDto(appointment, AppointmentDto.class);
                    List<PetDto> petDtos = appointment.getPets()
                                    .stream()
                                    .map(pet -> petEntityConverter.mapEntityToDto(pet, PetDto.class)).toList();
                    appointmentDto.setPets(petDtos);
                    return appointmentDto;
                }).toList();
    }
}
