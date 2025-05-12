import { useState } from 'react';
import { Button, ScrollView, StyleSheet } from 'react-native';
import NoCapTextInput from '../components/NoCapTextInput';
import { InAppInlineView } from '@dengage-tech/react-native-dengage';

export default function InAppInlineScreen() {
  const [propertyId, setPropertyId] = useState('1');
  const [screenName, setScreenName] = useState('inline');
  const [showInline, setShowInline] = useState(false);

  return (
    <ScrollView contentContainerStyle={styles.container}>
      <NoCapTextInput
        placeholder="Property ID"
        value={propertyId}
        onChangeText={setPropertyId}
      />
      <NoCapTextInput
        placeholder="Screen Name"
        value={screenName}
        onChangeText={setScreenName}
      />
      <Button title="Show InApp Inline" onPress={() => setShowInline(true)} />
      {showInline && (
        <InAppInlineView
          propertyId={propertyId}
          screenName={screenName}
          customParams={{}}
          style={styles.inline}
        />
      )}
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: { padding: 16 },
  inline: { marginTop: 16, height: 244 },
});
