import UIKit
import React
import Dengage

@objc public class RCTStoriesListView: RCTView {

    @objc public weak var bridge: RCTBridge?

    @objc public var storyPropertyId: String? {
        didSet { scheduleApplyStory() }
    }
    @objc public var screenName: String? {
        didSet { scheduleApplyStory() }
    }
    @objc public var customParams: [String: String]? {
        didSet { scheduleApplyStory() }
    }
    @objc public var hideIfNotFound: Bool = true {
        didSet { scheduleApplyStory() }
    }

    @objc public var onStoryVisibilityChanged: RCTDirectEventBlock?

    private let containerView: UIView = {
        let view = UIView()
        view.translatesAutoresizingMaskIntoConstraints = false
        view.backgroundColor = .clear
        view.isUserInteractionEnabled = true
        return view
    }()

    /// Pre-embedded list view passed into the SDK (matches native iOS + Flutter integrations).
    private let embeddedStoriesListView: StoriesListView = {
        let view = StoriesListView()
        view.translatesAutoresizingMaskIntoConstraints = false
        view.isUserInteractionEnabled = true
        return view
    }()

    private var activeStoriesListView: StoriesListView?
    private var lastMeasuredWidth: CGFloat = 0
    private var lastAppliedConfigurationSignature: String?

    private var pendingApplyWorkItem: DispatchWorkItem?
    private var pendingShowWorkItem: DispatchWorkItem?

    private var pollTimer: Timer?
    private var lastReportedHidden: Bool?
    private var hiddenSinceUptime: TimeInterval?
    private let hiddenDebounceSec: TimeInterval = 0
    private let pollIntervalSec: TimeInterval = 0.05

    override init(frame: CGRect) {
        super.init(frame: frame)
        commonInit()
    }

    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        commonInit()
    }

    deinit {
        stopVisibilityPolling()
    }

    private func commonInit() {
        backgroundColor = .clear
        isUserInteractionEnabled = true
        addSubview(containerView)
        NSLayoutConstraint.activate([
            containerView.leadingAnchor.constraint(equalTo: leadingAnchor),
            containerView.trailingAnchor.constraint(equalTo: trailingAnchor),
            containerView.topAnchor.constraint(equalTo: topAnchor),
            containerView.bottomAnchor.constraint(equalTo: bottomAnchor),
        ])

        containerView.addSubview(embeddedStoriesListView)
        NSLayoutConstraint.activate([
            embeddedStoriesListView.topAnchor.constraint(equalTo: containerView.topAnchor),
            embeddedStoriesListView.bottomAnchor.constraint(equalTo: containerView.bottomAnchor),
            embeddedStoriesListView.leadingAnchor.constraint(equalTo: containerView.leadingAnchor),
            embeddedStoriesListView.trailingAnchor.constraint(equalTo: containerView.trailingAnchor),
        ])
    }

    public override func hitTest(_ point: CGPoint, with event: UIEvent?) -> UIView? {
        guard isUserInteractionEnabled, !isHidden, alpha >= 0.01 else {
            return nil
        }
        let pointInContainer = containerView.convert(point, from: self)
        if let hit = containerView.hitTest(pointInContainer, with: event) {
            return hit
        }
        return bounds.contains(point) ? nil : super.hitTest(point, with: event)
    }

    public override func didMoveToWindow() {
        super.didMoveToWindow()
        if window != nil {
            scheduleApplyStory()
            startVisibilityPolling()
        } else {
            stopVisibilityPolling()
        }
    }

    private func configurationSignature() -> String? {
        guard let propertyId = storyPropertyId, let screenName = screenName else { return nil }
        let params = customParams ?? [:]
        let paramsPart = params.keys.sorted().map { key in
            "\(key)=\(params[key] ?? "")"
        }.joined(separator: "\u{1}")
        return "\(propertyId)\u{0}\(screenName)\u{0}\(paramsPart)\u{0}\(hideIfNotFound)"
    }

    private func scheduleApplyStory() {
        pendingApplyWorkItem?.cancel()
        pendingShowWorkItem?.cancel()
        let item = DispatchWorkItem { [weak self] in
            self?.applyStoryIfReady()
        }
        pendingApplyWorkItem = item
        DispatchQueue.main.async(execute: item)
    }

    private func resetStoryContent() {
        lastReportedHidden = nil
        hiddenSinceUptime = nil
        activeStoriesListView?.clearContent()
        activeStoriesListView = nil
        embeddedStoriesListView.clearContent()
        notifyReactNativeLayout()
    }

    private var isStoryEffectivelyHidden: Bool {
        guard let activeStoriesListView else { return false }
        return activeStoriesListView.isHidden
    }

    private func applyStoryIfReady() {
        guard window != nil else { return }
        guard let signature = configurationSignature() else { return }
        if signature == lastAppliedConfigurationSignature {
            return
        }
        let reloading = lastAppliedConfigurationSignature != nil
        if reloading {
            resetStoryContent()
        }
        lastAppliedConfigurationSignature = signature

        let runShow = { [weak self] in
            guard let self = self else { return }
            guard self.configurationSignature() == signature else { return }
            Dengage.showAppStory(
                storyPropertyID: self.storyPropertyId,
                storiesListView: self.embeddedStoriesListView,
                screenName: self.screenName,
                customParams: self.customParams,
                hideIfNotFound: self.hideIfNotFound
            ) { storiesListView in
                guard let storiesListView else {
                    self.activeStoriesListView = nil
                    let hidden = self.hideIfNotFound
                    self.lastReportedHidden = hidden
                    self.applyCollapsedNativeLayout(hidden: hidden)
                    self.onStoryVisibilityChanged?(["isHidden": hidden])
                    return
                }
                self.activeStoriesListView = storiesListView
                self.ensureStoryInteractionEnabled()
                self.updateVisibleHeightIfNeeded(force: true)
                self.emitVisibilityFromCurrentState()
                for delay in [0.05, 0.2, 0.5] {
                    DispatchQueue.main.asyncAfter(deadline: .now() + delay) { [weak self] in
                        self?.ensureStoryInteractionEnabled()
                        self?.updateVisibleHeightIfNeeded(force: delay == 0.5)
                        self?.emitVisibilityFromCurrentState()
                    }
                }
            }
        }

        if reloading {
            pendingShowWorkItem?.cancel()
            let showItem = DispatchWorkItem { runShow() }
            pendingShowWorkItem = showItem
            DispatchQueue.main.async(execute: showItem)
        } else {
            runShow()
        }
    }

    private func measureActiveStoryHeight() -> CGFloat {
        guard let storiesListView = activeStoriesListView, !isStoryEffectivelyHidden else {
            return 0
        }
        let width = bounds.width > 0
            ? bounds.width
            : (superview?.bounds.width ?? UIScreen.main.bounds.width)
        storiesListView.setNeedsLayout()
        storiesListView.layoutIfNeeded()

        let size = storiesListView.systemLayoutSizeFitting(
            CGSize(width: width, height: UIView.layoutFittingCompressedSize.height),
            withHorizontalFittingPriority: .required,
            verticalFittingPriority: .fittingSizeLevel
        )
        return max(size.height, 1)
    }

    private func ensureStoryInteractionEnabled() {
        guard let storiesListView = activeStoriesListView else { return }
        storiesListView.isUserInteractionEnabled = true
        containerView.isUserInteractionEnabled = true
        for subview in storiesListView.subviews {
            subview.isUserInteractionEnabled = true
            if let collectionView = subview as? UICollectionView {
                collectionView.allowsSelection = true
                collectionView.isScrollEnabled = true
            }
        }
    }

    private func updateVisibleHeightIfNeeded(force: Bool = false) {
        if isStoryEffectivelyHidden {
            notifyReactNativeLayout()
            return
        }

        let width = bounds.width > 0
            ? bounds.width
            : (superview?.bounds.width ?? 0)
        guard width > 0 else { return }

        let widthChanged = abs(width - lastMeasuredWidth) > 0.5
        guard force || widthChanged else { return }

        lastMeasuredWidth = width
        notifyReactNativeLayout()
    }

    private func notifyReactNativeLayout() {
        let height = isStoryEffectivelyHidden ? 0 : measureActiveStoryHeight()
        let width = bounds.width > 0 ? bounds.width : UIView.noIntrinsicMetric
        let intrinsicSize = CGSize(width: width, height: height)
        bridge?.uiManager.setIntrinsicContentSize(intrinsicSize, for: self)
        invalidateIntrinsicContentSize()
    }

    public override func layoutSubviews() {
        super.layoutSubviews()
        updateVisibleHeightIfNeeded()
    }

    private func emitVisibilityFromCurrentState() {
        let hidden = isStoryEffectivelyHidden
        if lastReportedHidden == nil || lastReportedHidden != hidden {
            lastReportedHidden = hidden
            applyCollapsedNativeLayout(hidden: hidden)
            onStoryVisibilityChanged?(["isHidden": hidden])
        }
    }

    private func applyCollapsedNativeLayout(hidden: Bool) {
        if !hidden {
            updateVisibleHeightIfNeeded(force: true)
        }
        notifyReactNativeLayout()
    }

    private func startVisibilityPolling() {
        stopVisibilityPolling()
        lastReportedHidden = nil
        hiddenSinceUptime = nil
        let timer = Timer(timeInterval: pollIntervalSec, repeats: true) { [weak self] _ in
            guard let self = self else { return }
            let rawHidden = self.isStoryEffectivelyHidden
            let now = ProcessInfo.processInfo.systemUptime
            let debouncedHidden: Bool
            if rawHidden {
                if self.hiddenSinceUptime == nil {
                    self.hiddenSinceUptime = now
                }
                debouncedHidden = (now - (self.hiddenSinceUptime ?? now)) >= self.hiddenDebounceSec
            } else {
                self.hiddenSinceUptime = nil
                debouncedHidden = false
            }
            if self.lastReportedHidden == nil || self.lastReportedHidden != debouncedHidden {
                self.lastReportedHidden = debouncedHidden
                self.applyCollapsedNativeLayout(hidden: debouncedHidden)
                self.onStoryVisibilityChanged?(["isHidden": debouncedHidden])
            }
        }
        RunLoop.main.add(timer, forMode: .common)
        pollTimer = timer
    }

    private func stopVisibilityPolling() {
        pollTimer?.invalidate()
        pollTimer = nil
    }

    public override var intrinsicContentSize: CGSize {
        if isStoryEffectivelyHidden {
            return CGSize(width: UIView.noIntrinsicMetric, height: 0)
        }
        return CGSize(width: UIView.noIntrinsicMetric, height: measureActiveStoryHeight())
    }
}
