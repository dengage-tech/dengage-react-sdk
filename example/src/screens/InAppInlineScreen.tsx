import { useEffect, useRef, useState } from 'react';
import {
  SafeAreaView,
  View,
  TextInput,
  Button,
  StyleSheet,
  KeyboardAvoidingView,
  Platform,
  ScrollView,
} from 'react-native';
import Dengage, { InAppInlineView } from '@dengage-tech/react-native-dengage';

export default function InAppInlineScreen() {
  const [propertyId, setPropertyId] = useState<string>('3');
  const [screenName, setScreenName] = useState<string>('gizem7');
  const [customKey1, setCustomKey1] = useState<string>('');
  const [customValue1, setCustomValue1] = useState<string>('');
  const [customKey2, setCustomKey2] = useState<string>('');
  const [customValue2, setCustomValue2] = useState<string>('');
  const [showInline, setShowInline] = useState(false);

  const getCustomParams = (): Record<string, string> => {
    const params: Record<string, string> = {};
    if (customKey1.trim() && customValue1.trim()) {
      params[customKey1.trim()] = customValue1.trim();
    }
    if (customKey2.trim() && customValue2.trim()) {
      params[customKey2.trim()] = customValue2.trim();
    }
    return params; // Always return an object, never null
  };


  const onPressShow = () => {
    // optional: call the native InApp API
    // Dengage.showInlineInApp?.({
    //   screenName: screenName.trim() || null,
    //   propertyId: propertyId.trim(),
    //   customParams: getCustomParams(),
    //   // inAppInlineElement: webviewRef.current
    // });
    setShowInline(true);
  };

  return (
    <SafeAreaView style={styles.flex}>
      <KeyboardAvoidingView
        style={styles.flex}
        behavior={Platform.OS === 'ios' ? 'padding' : undefined}
      >
        <ScrollView contentContainerStyle={styles.container}>
        

          <TextInput
            style={styles.input}
            placeholder="Property ID"
            value={propertyId}
            onChangeText={setPropertyId}
            autoCapitalize="none"
          />
          <TextInput
            style={styles.input}
            placeholder="Screen Name"
            value={screenName}
            onChangeText={setScreenName}
            autoCapitalize="none"
          />

          <View style={styles.row}>
            <TextInput
              style={styles.flexInput}
              placeholder="Custom Key 1"
              value={customKey1}
              onChangeText={setCustomKey1}
              autoCapitalize="none"
            />
            <View style={styles.spacer} />
            <TextInput
              style={styles.flexInput}
              placeholder="Custom Value 1"
              value={customValue1}
              onChangeText={setCustomValue1}
              autoCapitalize="none"
            />
          </View>

          <View style={styles.row}>
            <TextInput
              style={styles.flexInput}
              placeholder="Custom Key 2"
              value={customKey2}
              onChangeText={setCustomKey2}
            />
            <View style={styles.spacer} />
            <TextInput
              style={styles.flexInput}
              placeholder="Custom Value 2"
              value={customValue2}
              onChangeText={setCustomValue2}
            />
          </View>

          <Button title="Show InApp Inline" onPress={onPressShow} />

          {showInline && propertyId.trim() !== '' && (
          <View style={styles.flex1}>
            <InAppInlineView
              propertyId={propertyId.trim() || ""} // Empty string instead of null
              screenName={screenName.trim() || ""} // Empty string instead of null
              customParams={getCustomParams()}     // Always an object
              style={styles.flex1}
            />
          </View>
        )}
        </ScrollView>
      </KeyboardAvoidingView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  flex: { flex: 1, backgroundColor: '#fff000', },
  flex1: { flex: 1 },
  container: {
    padding: 16,
    backgroundColor: '#fff',
  },
  input: {
    height: 48,
    borderColor: '#ccc',
    borderWidth: 1,
    borderRadius: 4,
    paddingHorizontal: 12,
    fontSize: 16,
    marginBottom: 12,
  },
  row: {
    flexDirection: 'row',
    marginBottom: 12,
  },
  flexInput: {
    flex: 1,
    height: 48,
    borderColor: '#ccc',
    borderWidth: 1,
    borderRadius: 4,
    paddingHorizontal: 12,
  },
  spacer: { width: 12 },
});