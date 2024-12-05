import React, { useState } from 'react';
import { View, Text, StyleSheet, TouchableOpacity } from 'react-native';
import { TextInput, Button } from 'react-native-paper';
import { login, signup } from './api';
import AsyncStorage from '@react-native-async-storage/async-storage';

const Login = () => {
    const [formData, setFormData] = useState({ fullName: '', email: '', password: '' });
    const [loading, setLoading] = useState(false);
    const [isSignup, setIsSignup] = useState(false);
    const [isAuthenticated, setIsAuthenticated] = useState(false);

    const handleSubmit = async () => {
        setLoading(true);
        try {
            if (isSignup) {
                await signup(formData);
                setIsSignup(false);
            } else {
                const { data } = await login(formData);
                if (data.token) {
                    await AsyncStorage.setItem('token', data.token);
                    setIsAuthenticated(true);
                    setTimeout(() => {
                        window.location.reload();
                    }, 500);
                }
            }
        } catch (error: any) {
            console.error(error.response?.data || 'Une erreur est survenue');
            alert(error.response?.data.description)
        }
        setLoading(false);
    };

    if (isAuthenticated) {
        return (
            <View style={styles.container}>
                <Text style={styles.title}>Bienvenue, vous êtes connecté !</Text>
            </View>
        );
    }

    return (
        <View style={styles.container}>
            <Text style={styles.title}>{isSignup ? 'Créer un compte' : 'Se connecter'}</Text>

            {isSignup && (
                <TextInput
                    label="Nom complet"
                    value={formData.fullName}
                    onChangeText={(text) => setFormData({ ...formData, fullName: text })}
                    style={styles.input}
                />
            )}
            <TextInput
                label="Email"
                value={formData.email}
                onChangeText={(text) => setFormData({ ...formData, email: text })}
                style={styles.input}
                keyboardType="email-address"
                autoCapitalize="none"
            />
            <TextInput
                label="Mot de passe"
                value={formData.password}
                onChangeText={(text) => setFormData({ ...formData, password: text })}
                style={styles.input}
                secureTextEntry
            />

            <Button
                mode="contained"
                onPress={handleSubmit}
                loading={loading}
                style={styles.button}
            >
                {isSignup ? 'S\'inscrire' : 'Se connecter'}
            </Button>

            <View style={styles.signupLinkContainer}>
                <Text>{isSignup ? 'Vous avez déjà un compte ?' : 'Pas encore de compte ?'}</Text>
                <TouchableOpacity onPress={() => setIsSignup(!isSignup)}>
                    <Text style={styles.signupLink}>{isSignup ? 'Se connecter' : 'Créer un compte'}</Text>
                </TouchableOpacity>
            </View>
        </View>
    );
};

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        padding: 16,
        backgroundColor: '#fff',
    },
    title: {
        fontSize: 24,
        fontWeight: 'bold',
        textAlign: 'center',
        marginBottom: 24,
    },
    input: {
        marginBottom: 12,
    },
    button: {
        marginTop: 12,
    },
    signupLinkContainer: {
        flexDirection: 'row',
        justifyContent: 'center',
        marginTop: 16,
    },
    signupLink: {
        color: '#007bff',
        fontWeight: 'bold',
    },
});

export default Login;
