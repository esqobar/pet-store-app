import React, {useEffect, useState} from 'react';
import {Col, Form} from "react-bootstrap";
import AddItemModal from "../modals/AddItemModal.jsx";
import {getAllPetBreeds} from "./PetService.js";


const PetBreedSelector = ({ petType, value, onChange }) => {
    const [petBreeds, setPetBreeds] = useState([])
    const [showModal, setShowModal] = useState(false)

    useEffect(() => {
        if (petType) {
            const fetchAllPetBreeds = async () => {
                try {
                    const result = await getAllPetBreeds(petType)
                    setPetBreeds(result.data);
                } catch (error) {
                    console.error(error.message);
                }
            };
            fetchAllPetBreeds();
        } else {
            setPetBreeds([]);
        }
    }, [petType]);

    //1. handling color changes
    const handleBreedChange = (e) => {
        if (e.target.value === 'add-new-item') {
            setShowModal(true)
        } else {
            onChange(e)
        }
    }

    //2. handling the save new item
    const handleSaveNewItem = (newItem) => {
        if (newItem && !petBreeds.includes(newItem)) {
            setPetBreeds([...petBreeds, newItem])
            onChange({ target: { name: "petBreed", value: newItem } })
        }
    }

    return (
        <React.Fragment>
            <Form.Group as={Col} controlId='petBreed'>
                <Form.Control as='select' name='petBreed'
                              value={value} required onChange={handleBreedChange}>
                    <option value=''>Select Breed</option>
                    <option value='add-new-item'>Add a new Breed</option>
                    { petBreeds.map((breed) => (
                        <option key={breed} value={breed}>{breed}</option>
                    ))}
                </Form.Control>
            </Form.Group>
            <AddItemModal
                show={showModal} handleClose={() => setShowModal(false)} handleSave={handleSaveNewItem}
                itemLabel={'Breed'}
            />
        </React.Fragment>
    );
};

export default PetBreedSelector;