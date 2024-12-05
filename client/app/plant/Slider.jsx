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
        transform: 'none !important',
        marginTop : -10,
        '&::before': {
            position: 'absolute',
            content: '""',
            width: '8px',
            height: '8px',
            transform: 'translate(-50%, -50%) rotate(45deg)',
            backgroundColor: 'inherit',
            left: '-1px',
            top: '50%',
        },
        left: '30px !important',
        right: 'unset',
    },
}));

function valuetext(value: number) {
    return `${value}%`;
}

export default function SliderComponent({ humidityRate }) {
    const [value, setValue] = useState(humidityRate);

    useEffect(() => {
        setValue(humidityRate);
    }, [humidityRate]);

    const handleSliderChange = (event: Event, newValue: any | any[]) => {
        setValue(newValue);
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
