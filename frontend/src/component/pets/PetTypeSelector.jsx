import React, {useEffect, useState} from 'react';
import {Col, Form} from "react-bootstrap";
import AddItemModal from "../modals/AddItemModal.jsx";
import {getAllPetTypes} from "./PetService.js";

const PetTypeSelector = ({ value, onChange }) => {
    const [petTypes, setPetTypes] = useState([])
    const [showModal, setShowModal] = useState(false)

    useEffect(() => {
        const fetchAllPetTypes = async () => {
            try {
                const result = await getAllPetTypes()
                setPetTypes(result.data);
            } catch (error) {
                console.error(error.message);
            }
        };
        fetchAllPetTypes();
    }, []);

    //1. handling color changes
    const handleTypeChange = (e) => {
        if (e.target.value === 'add-new-item') {
            setShowModal(true)
        } else {
            onChange(e)
        }
    }

    //2. handling the save new item
    const handleSaveNewItem = (newItem) => {
        if (newItem && !petTypes.includes(newItem)) {
            setPetTypes([...petTypes, newItem])
            onChange({ target: { name: "petType", value: newItem } })
        }
    }

    return (
        <React.Fragment>
            <Form.Group as={Col} controlId='petType'>
                <Form.Control as='select' name='petType'
                              value={value} required onChange={handleTypeChange}>
                    <option value=''>Select Type</option>
                    <option value='add-new-item'>Add a new Type</option>
                    { petTypes.map((type) => (
                        <option key={type} value={type}>{type}</option>
                    ))}
                </Form.Control>
            </Form.Group>
            <AddItemModal
                show={showModal} handleClose={() => setShowModal(false)} handleSave={handleSaveNewItem}
                itemLabel={'Type'}
            />
        </React.Fragment>
    );
};

export default PetTypeSelector;