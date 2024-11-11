import React, {useEffect, useState} from "react";
import {Col, Form} from "react-bootstrap";
import AddItemModal from "../modals/AddItemModal.jsx";
import {getAllPetColors} from "./PetService.js";

const PetColorSelector = ({ value, onChange }) => {
    const [petColors, setPetColors] = useState([])
    const [showModal, setShowModal] = useState(false)

    useEffect(() => {
        const fetchAllPetColors = async () => {
            try {
                const response = await getAllPetColors()
                setPetColors(response.data);
            } catch (error) {
                console.error(error.message);
            }
        };
        fetchAllPetColors();
    }, []);

    //1. handling color changes
    const handleColorChange = (e) => {
        if (e.target.value === 'add-new-item') {
            setShowModal(true)
        } else {
            onChange(e)
        }
    }

    //2. handling the save new item
    const handleSaveNewItem = (newItem) => {
        if (newItem && !petColors.includes(newItem)) {
            setPetColors([...petColors, newItem])
            onChange({ target: { name: "petColor", value: newItem } })
        }
    }

    return (
        <React.Fragment>
            <Form.Group as={Col} controlId='petColor'>
                <Form.Control as='select' name='petColor'
                              value={value} required onChange={handleColorChange}>
                    <option value=''>Select Color</option>
                    <option value='add-new-item'>Add a new Color</option>
                    {petColors.map((color) => (
                        <option key={color} value={color}>
                            {color}
                        </option>
                    ))}
                </Form.Control>
            </Form.Group>
            <AddItemModal
                show={showModal} handleClose={() => setShowModal(false)} handleSave={handleSaveNewItem}
                itemLabel={'Color'}
            />
        </React.Fragment>
    );
};

export default PetColorSelector;