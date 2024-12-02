import * as React from 'react';
import Stack from '@mui/material/Stack';
import Slider from '@mui/material/Slider';
import { styled } from '@mui/system';
import {useEffect, useState} from "react";

const CustomSlider = styled(Slider)(({ theme }) => ({
    '& .MuiSlider-valueLabel': {

        fontSize: 12,
        fontWeight: 'normal',
        display: 'flex',
        transform: 'none !important', // Réinitialiser les transformations par défaut
        marginTop : -10,
        '&::before': {
            position: 'absolute',
            content: '""',
            width: '8px',
            height: '8px',
            transform: 'translate(-50%, -50%) rotate(45deg)', // Applique la rotation
            backgroundColor: 'inherit',
            left: '-1px',
            top: '50%',
        },
        // Ajout de la surcharge pour left au lieu de right
        left: '30px !important', // Remplacer right par left
        right: 'unset', // Annuler la propriété right si elle est définie
    },
}));

function valuetext(value: number) {
    return `${value}%`; // Affiche la valeur en pourcentage
}

export default function SliderComponent({ humidityRate }) {
    const [value, setValue] = useState(humidityRate);

    useEffect(() => {
        setValue(humidityRate);  // Met à jour la valeur si la prop change
    }, [humidityRate]);

    const handleSliderChange = (event: Event, newValue: any | any[]) => {
        setValue(newValue); // Met à jour l'état avec la nouvelle valeur du slider
    };

    return (
        <Stack
            sx={{
                height: 275,
                width: 40,
                background: 'linear-gradient(to top, red, aqua)',
                borderRadius: 10,
                position: 'relative',
                paddingTop: 2,
                paddingBottom: 2,
                marginLeft: 2
            }}
            spacing={1}
            direction="row"
        >
            <CustomSlider
                aria-label="Progression"
                orientation="vertical"
                value={value}
                onChange={handleSliderChange}
                valueLabelDisplay="auto"
                valueLabelFormat={valuetext}
                disabled
                sx={{
                    '& .MuiSlider-rail': {
                        display: 'none',
                        border: 'none',
                    },
                    '& .MuiSlider-track': {
                        backgroundColor: 'transparent',
                        border: 'none',
                    },
                    '& .MuiSlider-thumb': {
                        border: 'none',
                        width: 30,
                        height: 30,
                        cursor: 'pointer',
                    },
                }}
            />
        </Stack>
    );
}
