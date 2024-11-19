import React, { useEffect, useState } from "react";
import { ThemedText } from "@/components/ThemedText";
import { FaCloudUploadAlt } from "react-icons/fa";

interface Props {
    handleFileChange: (file: File | null) => void; // Fonction pour transmettre le fichier au parent
    preview: string | null; // URL de l'aperçu
    errorMessage: string | undefined; // Message d'erreur si présent
}

const FilePreview: React.FC<Props> = ({ handleFileChange, preview, errorMessage }) => {
    const [localPreview, setLocalPreview] = useState(preview);

    useEffect(() => {
        setLocalPreview(preview); // Met à jour localPreview chaque fois que preview change
    }, [preview]);

    return (
        <label className="flex flex-col" style={styles.labelStyle}>
            <ThemedText>
                <span className="font-medium">Importer une image :</span>
            </ThemedText>
            <div style={styles.imageStyle}>
                {/* Input fichier */}
                <input
                    type="file"
                    accept="image/*"
                    onChange={(e) => {
                        const file = e.target.files?.[0] || null;
                        handleFileChange(file); // Appel de la fonction du parent
                    }}
                    style={styles.inputFile}
                />
                <div style={styles.iconContainer}>
                    <FaCloudUploadAlt style={styles.iconStyle} />
                </div>
            </div>
            <div>
                {/* Aperçu de l'image */}
                {localPreview && <img style={styles.PreviewStyle} src={localPreview} alt="Aperçu" />}
            </div>
            {/* Affichage des erreurs */}
            {errorMessage && <span className="warningStyle">{errorMessage}</span>}
        </label>
    );
};

const styles = {
    inputFile: {
        opacity: 0,
    },
    labelStyle: {
        color: "white",
        display: "flex",
        flexDirection: "column",
        marginTop: 16,
    },
    imageStyle: {
        color: "white",
        borderRadius: "5px",
        cursor: "pointer",
        textAlign: "center",
        height: 100,
        paddingVertical: 8,
        paddingLeft: 8,
        paddingRight: 8,
        marginTop: 8,
        marginBottom: 8,
        borderStyle: "dashed",
        display: "container",
    },
    warningStyle: {
        color: "orange",
    },
    PreviewStyle: {
        maxWidth: "100%",
        objectFit: "cover",
        height: "auto",
        borderRadius: 8,
        marginTop: 8,
        marginBottom: 8,
    },
    iconStyle: {
        fontSize: "60px", // Adjust the icon size
        color: "#ffffff", // Change the color of the icon if needed
    },
};

export default FilePreview;
