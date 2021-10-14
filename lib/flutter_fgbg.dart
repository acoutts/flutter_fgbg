import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';

enum FGBGType {
  foreground,
  background,
}

class FGBGEvents {
  FGBGEvents._internal() {
    _channel.receiveBroadcastStream().listen((event) {
      _controller.add(
        event == 'foreground' ? FGBGType.foreground : FGBGType.background,
      );
    });
  }
  factory FGBGEvents() => _instance;
  static final FGBGEvents _instance = FGBGEvents._internal();

  final _channel = EventChannel("com.ajinasokan.flutter_fgbg/events");
  final _controller = StreamController<FGBGType>.broadcast();

  Stream<FGBGType> get stream => _controller.stream;
}

class FGBGNotifier extends StatefulWidget {
  final Widget child;
  final ValueChanged<FGBGType> onEvent;

  FGBGNotifier({
    required this.child,
    required this.onEvent,
  });

  @override
  _FGBGNotifierState createState() => _FGBGNotifierState();
}

class _FGBGNotifierState extends State<FGBGNotifier> {
  StreamSubscription? subscription;

  @override
  void initState() {
    super.initState();
    subscription = FGBGEvents().stream.listen((event) {
      widget.onEvent.call(event);
    });
  }

  @override
  void dispose() {
    subscription?.cancel();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) => widget.child;
}
