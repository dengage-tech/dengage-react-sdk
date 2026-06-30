import { useState } from 'react';
import {
  View,
  Text,
  StyleSheet,
  TouchableOpacity,
  ScrollView,
  ActivityIndicator,
} from 'react-native';
import Dengage from '@dengage-tech/react-native-dengage';

export default function SubscriptionScreen() {
  const [subscriptionText, setSubscriptionText] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const showSubscription = async () => {
    setLoading(true);
    setError('');
    setSubscriptionText('');

    try {
      const sub = await Dengage.getSubscription();
      const formatted = JSON.stringify(sub, null, 2);
      console.log('getSubscription:', formatted);
      setSubscriptionText(formatted);
    } catch (err) {
      const message = err instanceof Error ? err.message : String(err);
      console.error('getSubscription error:', message);
      setError(message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <View style={styles.container}>
      <TouchableOpacity
        style={styles.button}
        onPress={showSubscription}
        disabled={loading}>
        <Text style={styles.buttonText}>SHOW SUBSCRIPTION</Text>
      </TouchableOpacity>

      {loading && <ActivityIndicator style={styles.loader} size="large" />}

      {error ? <Text style={styles.error}>{error}</Text> : null}

      {subscriptionText ? (
        <ScrollView style={styles.outputScroll}>
          <Text style={styles.output}>{subscriptionText}</Text>
        </ScrollView>
      ) : null}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 16,
    backgroundColor: '#ffffff',
  },
  button: {
    padding: 16,
    backgroundColor: '#d3d3d3',
    alignItems: 'center',
    borderRadius: 8,
  },
  buttonText: {
    fontSize: 16,
    fontWeight: 'bold',
  },
  loader: {
    marginTop: 24,
  },
  error: {
    marginTop: 16,
    color: '#c00',
    fontSize: 14,
  },
  outputScroll: {
    marginTop: 16,
    flex: 1,
    backgroundColor: '#f5f5f5',
    borderRadius: 8,
    padding: 12,
  },
  output: {
    fontFamily: 'Menlo',
    fontSize: 12,
  },
});
