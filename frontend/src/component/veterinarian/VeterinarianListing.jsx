import {useEffect, useState} from "react";
import {Col, Container, Row} from "react-bootstrap";
import VeterinarianCard from "./VeterinarianCard.jsx";
import {getVeterinarians} from "./VeterinarianService.js";
import VeterinarianSearch from "./VeterinarianSearch.jsx";
import useMessageAlerts from "../hooks/UseMessageAlerts.js";


const VeterinarianListing = () => {
    const [veterinarians, setVeterinarians] = useState([])
    const [allVeterinarians, setAllVeterinarians] = useState([])
    const {errorMessage, setErrorMessage, showErrorAlert, setShowErrorAlert} = useMessageAlerts()

    useEffect(() => {
        getVeterinarians().then((data) => {
            setVeterinarians(data.data)
            setAllVeterinarians(data.data)
        }).catch((error) => {
            setErrorMessage(error.response.data.message)
            setShowErrorAlert(true)
        })
    }, [])

    const handleSearchResult = (veterinarians) => {
        if (veterinarians === null) {
            setVeterinarians(allVeterinarians)
        } else if (Array.isArray(veterinarians) && veterinarians.length > 0) {
            setVeterinarians(veterinarians)
        } else {
            setVeterinarians([])
        }
    }

    if (veterinarians.length === 0) {
        return <p>No Veterinarians Found At This Time</p>
    }

    return (
        <Container>
            <Row className="justify-content-center">
                <h6 className='text-center mb-4 mt-4'>Meet Our Veterinarians</h6>
            </Row>
            <Row className="justify-content-center">
                <Col md={4}>
                    <VeterinarianSearch onSearchResult={handleSearchResult} />
                </Col>
                <Col md={7}>
                    {veterinarians.map((vet, index) => (
                        <VeterinarianCard key={index} vet={vet} />
                    ))}
                </Col>
            </Row>
        </Container>
    );
};

export default VeterinarianListing;