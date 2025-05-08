import React, { useState } from 'react'
import {
  TextInput,
  Button,
  ScrollView,
  StyleSheet,
} from 'react-native'
import { InAppInlineView } from '@dengage-tech/react-native-dengage';

export default function InAppInlineScreen() {
  const [propertyId, setPropertyId] = useState('1')
  const [screenName, setScreenName] = useState('inline')
  const [customParams, setCustomParams] = useState({})  
  const [showInline, setShowInline] = useState(false)

  return (
    <ScrollView contentContainerStyle={styles.container}>
      <TextInput
        style={styles.input}
        placeholder="Property ID"
        value={propertyId}
        onChangeText={setPropertyId}
      />
      <TextInput
        style={styles.input}
        placeholder="Screen Name"
        value={screenName}
        onChangeText={setScreenName}
      />

      {/* if you have dynamic key/value inputs, render them here and update customParams */}

      <Button
        title="Show InApp Inline"
        onPress={() => setShowInline(true)}
      />

      {showInline && (
        <InAppInlineView
          propertyId={propertyId}
          screenName={screenName}
          customParams={customParams}
          style={styles.inline}
        />
      )}
    </ScrollView>
  )
}

const styles = StyleSheet.create({
  container: {
    padding: 16,
  },
  input: {
    height: 50,
    borderWidth: 1,
    borderRadius: 6,
    paddingHorizontal: 12,
    marginBottom: 12,
  },
  inline: {
    marginTop: 16,
    // no height here!
  },
})